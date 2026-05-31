package com.example.__Bit_Microcontrolller_Assembler.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class SerialPortCom {

    public void sendInstructions(String instruction) throws IOException {
        SerialPort port = SerialPort.getCommPort("COM3");
        port.setBaudRate(115200);
        boolean opened = port.openPort();
        for (SerialPort p : SerialPort.getCommPorts()) {
            System.out.println(
                    p.getSystemPortName() + " - " +
                            p.getDescriptivePortName()
            );
        }
        for (SerialPort p : SerialPort.getCommPorts()) {
            System.out.println("Port: " + p.getSystemPortName());
            System.out.println("Description: " + p.getDescriptivePortName());
            System.out.println("Path: " + p.getSystemPortPath());
            System.out.println("----------------");
        }

        System.out.println("Opened: " + opened);

        OutputStream out = port.getOutputStream();
        if (!opened) {
            throw new RuntimeException("Failed to open port");
        }

        out.write("00001111111,111110101010\n".getBytes());
        out.flush();


    }
}
