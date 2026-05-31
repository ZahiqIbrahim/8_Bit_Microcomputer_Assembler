package com.example.__Bit_Microcontrolller_Assembler;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.OutputStream;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);

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

		out.write("111110101010,111100001111\n".getBytes());
		out.flush();


	}
}
