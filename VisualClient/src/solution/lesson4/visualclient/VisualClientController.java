/* Copyright © 2014, Oracle and/or its affiliates. All rights reserved. 
 * VisualClientController.java
 */
package solution.lesson4.visualclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.time.ZoneOffset;
import java.util.ResourceBundle;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import static mooc.data.server.DataProtocol.DISCONNECT;
import static mooc.data.server.DataProtocol.READ_DATA;

/**
 * Controller class for the Visual Client application
 *
 * @author Jim Weaver @JavaFXpert
 */
public class VisualClientController implements Initializable {

    Socket _socket;
    String _host = "78.84.160.160"; // Change to actual address of service on Pi
    int _port = 8080; // Modify if connecting on a different port is desired

    InputStream _sis;
    BufferedReader _br;

    OutputStream _os;
    OutputStreamWriter _osw;

    ObservableList<SwitchActivation> _switchActivations = observableArrayList();
    ObservableList<TemperatureReading> _temperatureReadings = observableArrayList();
    ObservableList<PositionReading> _positionReadings = observableArrayList();
    ObservableList<MotionReading> _motionReadings = observableArrayList();

    NumberAxis _xAxis = new NumberAxis();
    NumberAxis _yAxis = new NumberAxis();

    XYChart.Series _seriesTemperature = new XYChart.Series();

    @FXML
    AreaChart<Number, Number> temperatureChart;

    @FXML
    TableView<PositionReading> positionTable;

    @FXML
    TableColumn dateTimeCol;

    @FXML
    TableColumn latitudeCol;

    @FXML
    TableColumn longitudeCol;

    @FXML
    TableColumn altitudeCol;

    @FXML
    Label statusLabel;

    @FXML
    Button connectButton;

    @FXML
    Button readDataButton;

    @FXML
    Button disconnectButton;

    @FXML
    private void handleConnectButtonClicked(ActionEvent ae) {
        setStatus("Connecting to server", false, false, false);
        try {
            _socket = new Socket(_host, _port);
            _sis = _socket.getInputStream();
            _br = new BufferedReader(new InputStreamReader(_sis));

            _os = _socket.getOutputStream();
            _osw = new OutputStreamWriter(_os);

            setStatus("Connected to server", false, true, true);
        } catch (IOException ioe) {
            setStatus("Problem connecting to server", true, false, false);
        }
    }

    @FXML
    private void handleReadDataButtonClicked(ActionEvent ae) {
        readDataFromServer();
    }

    @FXML
    private void handleDisconnectButtonClicked(ActionEvent ae) {
        setStatus("Attempting to disconnect from server", false, false, false);
        try {
            _osw.write(DISCONNECT);
            _osw.flush();
            setStatus("Not connected to server", true, false, false);
        } catch (IOException ioe) {
            setStatus("Problem disconnecting from server", true, false, false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        temperatureChart.getXAxis().setLabel("Seconds");
        temperatureChart.getYAxis().setLabel("Temperature");
        _seriesTemperature.setName("Temperatures by Second");
        temperatureChart.getData().add(_seriesTemperature);

        dateTimeCol.setCellValueFactory(
                new PropertyValueFactory<>("dateTimeFormatted")
        );

        latitudeCol.setCellValueFactory(
                new PropertyValueFactory<>("latitude")
        );

        longitudeCol.setCellValueFactory(
                new PropertyValueFactory<>("longitude")
        );

        altitudeCol.setCellValueFactory(
                new PropertyValueFactory<>("altitude")
        );

        positionTable.setItems(_positionReadings);

        setStatus("Not connected to server", true, false, false);
    }

    private void readDataFromServer() {
        setStatus("Attempting to read from server", false, false, false);
        removeSeriesTempuratureData();
        _positionReadings.clear();

        try {
            _osw.write(READ_DATA);
            _osw.flush();

            // TMM - loop until all of the strings are read
            // The first string is the number of records being sent
            String message = _br.readLine();
            int messLen = Integer.parseInt(message);
            System.out.println("number of messages: " + messLen);
            for (int i = 0; i < messLen; i++) {
                message = _br.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Message received: " + message);
                parseMessage(message);
            }
            setStatus("Data successfully read from server", false, true, true);
            chartTemperatureReadings();
        } catch (IOException | IllegalStateException | NumberFormatException e) {
            System.out.println("Problem in client " + e);
            setStatus("Problem reading data from server", false, false, true);
        }
    }

    private void parseMessage(String message) {
        if (message != null) {
            String[] messageSegments = message.split("\\^");
            int numSegments = messageSegments.length;
            // Don't attempt to parse non-existent messages
            if (numSegments >= 1) {
                parseSwitchActivations(messageSegments[0]);
            }
            if (numSegments >= 2) {
                parseTemperatureReadings(messageSegments[1]);
            }
            if (numSegments >= 3) {
                parsePositionReadings(messageSegments[2]);
            }
            if (numSegments >= 4) {
                parseMotionReadings(messageSegments[3]);
            }
        } else {
            throw new IllegalStateException("Message received is null");
        }
    }

    private void parseSwitchActivations(String switchActivationMessage) {
        String[] switchActivationSegments = switchActivationMessage.split(",");

        for (int i = 0; i < switchActivationSegments.length; i++) {
            if (i % 2 == 1) {
                SwitchActivation switchActReading
                        = new SwitchActivation(switchActivationSegments[i - 1],
                                switchActivationSegments[i]);
                System.out.println(switchActReading);
                _switchActivations.add(switchActReading);
            }
        }
    }

    private void parseTemperatureReadings(String temperatureMessage) {

        String[] temperatureSegments = temperatureMessage.split(",");

        for (int i = 0; i < temperatureSegments.length; i++) {
            if (i % 2 == 1) {
                TemperatureReading tempReading
                        = new TemperatureReading(temperatureSegments[i - 1],
                                temperatureSegments[i]);
                System.out.println(tempReading);
                _temperatureReadings.add(tempReading);

//                if (i == 1) {
//                    firstReadingEpochSecond = tempReading.getDateTime().toEpochSecond(ZoneOffset.UTC);
//                }
//                XYChart.Data xydata = new XYChart.Data("" + (tempReading.getDateTime().toEpochSecond(ZoneOffset.UTC) - firstReadingEpochSecond), tempReading.getTemperature());
//                _seriesTemperature.getData().add(xydata);
            }
        }
    }

    private void chartTemperatureReadings() {
        int i = 0;
        long firstReadingEpochSecond = 0;
        for (TemperatureReading tempReading : _temperatureReadings) {
            if (i == 0) {
                firstReadingEpochSecond = tempReading.getDateTime().toEpochSecond(ZoneOffset.UTC);
                i++;
            }
            XYChart.Data xydata = new XYChart.Data("" + (tempReading.getDateTime().toEpochSecond(ZoneOffset.UTC) - firstReadingEpochSecond), tempReading.getTemperature());
            _seriesTemperature.getData().add(xydata);
        }
    }

    private void parsePositionReadings(String positionMessage) {
        String[] positionSegments = positionMessage.split(",");

        for (int i = 0; i < positionSegments.length; i++) {
            if (i % 6 == 5) {
                PositionReading pos
                        = new PositionReading(positionSegments[i - 5],
                                positionSegments[i - 4],
                                positionSegments[i - 3],
                                positionSegments[i - 2],
                                positionSegments[i - 1],
                                positionSegments[i]);
                System.out.println(pos);
                _positionReadings.add(pos);
            }
        }
    }

    private void parseMotionReadings(String motionMessage) {
        String[] motionSegments = motionMessage.split(",");

        for (int i = 0; i < motionSegments.length; i++) {
            if (i % 3 == 2) {
                MotionReading mot
                        = new MotionReading(motionSegments[i - 2],
                                motionSegments[i - 1],
                                motionSegments[i]);
                System.out.println(mot);
                _motionReadings.add(mot);
            }
        }
    }

    private void removeSeriesTempuratureData() {
        int numTemps = _seriesTemperature.getData().size();
        for (int i = numTemps - 1; i > 0; i--) {
            _seriesTemperature.getData().remove(i);
        }
    }

    private void setStatus(String msg, boolean connectEnable,
            boolean readDataEnable, boolean disconnectEnable) {
        statusLabel.setText(msg);
        connectButton.setDisable(!connectEnable);
        readDataButton.setDisable(!readDataEnable);
        disconnectButton.setDisable(!disconnectEnable);
    }
}
