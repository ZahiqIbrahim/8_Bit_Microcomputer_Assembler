# 8-Bit Microcomputer Assembler

A Spring Boot-based assembler and programming interface for an 8-bit microcomputer. This application converts human-readable assembly instructions into machine opcodes, formats them for serial transmission, and uploads them to a Raspberry Pi Pico running C++ firmware that programs the 8-bit microcomputer's memory.

## Overview

This project provides a complete toolchain for programming a custom 8-bit microcomputer:

1. **Assembler**: Converts text-based assembly instructions to binary opcodes
2. **Instruction Formatter**: Formats opcodes for serial transmission
3. **Serial Communication**: Sends compiled programs to Raspberry Pi Pico via USB/UART
4. **REST API**: Web interface for submitting and uploading programs

The Raspberry Pi Pico acts as a bridge, receiving the compiled program via serial port and writing it to the 8-bit microcomputer's memory by setting appropriate pins.

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   Web Client    │────▶│  Spring Boot     │────▶│ Raspberry Pi    │
│                 │ API │   Assembler      │ UART│      Pico        │
└─────────────────┘     └──────────────────┘     └────────┬────────┘
                                                          │
                                                          │ GPIO
                                                          ▼
                                                  ┌───────────────┐
                                                  │ 8-Bit Micro-  │
                                                  │   computer    │
                                                  │    Memory     │
                                                  └───────────────┘
```

## System Requirements

### Software
- **Java**: 21 or higher
- **Maven**: 3.6+ (included via Maven Wrapper)
- **Spring Boot**: 4.0.6

### Hardware
- **Raspberry Pi Pico** with C++ firmware
- **8-Bit Microcomputer** with 16-byte memory
- **USB Connection**: For serial communication (configured as COM3)
- **Serial Port Settings**: 115200 baud rate

## Instruction Set

The assembler supports the following 8-bit instruction set:

### Memory & Arithmetic Instructions (4-bit opcode + 4-bit payload)

| Mnemonic | Opcode | Description | Payload Range |
|----------|--------|-------------|---------------|
| LDA      | 0001   | Load Accumulator | 00-15 (memory address) |
| ADD      | 0010   | Add to Accumulator | 00-15 (memory address) |
| SUB      | 0011   | Subtract from Accumulator | 00-15 (memory address) |
| STA      | 0100   | Store Accumulator | 00-15 (memory address) |
| LDI      | 0101   | Load Immediate | 00-15 (immediate value) |
| JMP      | 0110   | Jump | 00-15 (memory address) |
| JPC      | 0111   | Jump on Carry | 00-15 (memory address) |
| JPZ      | 1000   | Jump on Zero | 00-15 (memory address) |

### Control Instructions (8-bit opcode, no payload)

| Mnemonic | Opcode | Description |
|----------|--------|-------------|
| OUT      | 11100000 | Output accumulator |
| HLT      | 11110000 | Halt execution |

### Data Values

Direct numeric values (0-255) can be stored in memory. These are converted to 8-bit binary representation.

## Memory Layout

- **Total Memory**: 16 bytes (128 bits)
- **Address Range**: 00-15
- **Word Size**: 8 bits per instruction

## Project Structure

```
8_Bit_Microcomputer_Assembler/
├── src/main/java/com/example/__Bit_Microcontrolller_Assembler/
│   ├── Application.java              # Spring Boot entry point
│   ├── controller/
│   │   └── ProgramController.java    # REST API endpoint
│   └── service/
│       ├── Assembler.java            # Instruction to opcode conversion
│       ├── CodeLookUp.java           # Opcode/payload mapping
│       ├── InstructionFormatter.java # Serial formatting
│       └── SerialPortCom.java        # Serial communication
├── src/main/resources/
│   └── application.properties        # Application configuration
├── pom.xml                           # Maven dependencies
└── README.md                         # This file
```

## Installation

### Prerequisites

1. Install Java 21:
   ```bash
   # Verify installation
   java -version
   ```

2. Clone the repository:
   ```bash
   git clone <repository-url>
   cd 8_Bit_Microcontrolller_Assembler
   ```

### Build the Application

Using Maven Wrapper (included):
```bash
./mvnw clean install
```

Or using system Maven:
```bash
mvn clean install
```

### Configure Serial Port

Edit `src/main/java/com/example/__Bit_Microcontrolller_Assembler/service/SerialPortCom.java` to change the serial port if needed:

```java
SerialPort port = SerialPort.getCommPort("COM3");  // Change to your port
port.setBaudRate(115200);
```

## Running the Application

### Start the Server

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` by default.

### Verify Serial Ports

The application logs all available serial ports on startup. Check the console output to verify your Pico is detected.

## Usage

### Via REST API

Send a POST request to `/new-program` with your assembly code:

**Endpoint**: `POST http://localhost:8080/new-program`

**Content-Type**: `text/plain`

**Example Request Body**:
```
LDI 05
STA 00
LDA 00
ADD 01
OUT
HLT
```

**Example using cURL**:
```bash
curl -X POST http://localhost:8080/new-program \
     -H "Content-Type: text/plain" \
     -d "LDI 05
STA 00
LDA 00
ADD 01
OUT
HLT"
```

**Response**:
- Success: `200 OK` - "Program uploaded successfully."
- Error: `400 Bad Request` - Error message with details

### Assembly Language Syntax

1. **Instructions with payload**: `MNEMONIC XX`
   - Space between mnemonic and 2-digit payload
   - Payload must be 00-15
   - Example: `LDA 05`, `ADD 10`

2. **Instructions without payload**: `MNEMONIC`
   - No space or payload
   - Example: `OUT`, `HLT`

3. **Data values**: Just the number
   - 0-255
   - Example: `42`, `255`

### Example Programs

#### Simple Addition
```
LDI 10
STA 00
LDI 05
ADD 00
OUT
HLT
```
This loads 10 into memory address 0, then adds 5 to it and outputs the result (15).

#### Loop Counter
```
LDI 05
STA 00
LDA 00
SUB 01
JPZ 06
JMP 02
HLT
```
Counts down from 5 to 0.

## API Reference

### POST /new-program

Uploads and assembles a program for the 8-bit microcomputer.

**Request**:
- Method: POST
- Path: `/new-program`
- Headers: `Content-Type: text/plain`
- Body: Assembly program (one instruction per line)

**Response**:
- Success (200): `"Program uploaded successfully."`
- Error (400): JSON object with error message
  ```json
  {
    "Error": "Error message here"
  }
  ```

**Error Handling**:
The assembler validates:
- Memory capacity (max 16 instructions)
- Instruction validity
- Payload range (00-15 for instructions, 0-255 for data)
- Payload requirements for instructions that need them

## Error Messages

| Error | Cause |
|-------|-------|
| "Memory Capacity Exceeded - Memory is of 16 byte" | Program has more than 16 instructions |
| "stored value must not exceed 255" | Data value > 255 |
| "Invalid Instruction" | Unknown mnemonic |
| "Invalid PayLoad Or PayLoad Out Of Capacity" | Invalid or out-of-range payload |
| "No payload provided to an instruction that required one" | Missing payload for instruction |
| "OUT or HLT requires no payload" | Payload provided to OUT/HLT |
| "Failed to open port" | Serial port communication error |

## Raspberry Pi Pico Firmware

The Pico firmware (not included in this repository) should:

1. **Initialize UART** at 115200 baud
2. **Read serial data** in comma-separated binary format
3. **Parse instructions** (8-bit binary strings)
4. **Set GPIO pins** to write to the 8-bit microcomputer:
   - Address bus pins
   - Data bus pins
   - Control signals (read/write, clock, etc.)
5. **Write each instruction** to the appropriate memory address
6. **Acknowledge completion** back to the host

### Expected Serial Data Format

The assembler sends instructions as comma-separated 8-bit binary strings followed by newline:

```
01010000,01000000,00010000,00100001,11100000,11110000\n
```

Each 8-bit string represents one instruction or data value.

## Dependencies

- **Spring Boot Starter Web MVC**: Web framework and REST API
- **jSerialComm 2.11.0**: Cross-platform serial port communication

## Troubleshooting

### Serial Port Issues

1. **Port not found**: Check that the Raspberry Pi Pico is connected and recognized by your OS
2. **Permission denied**: Ensure your user has permissions to access the serial port
3. **Wrong port**: Update the port name in `SerialPortCom.java` (e.g., COM3, /dev/ttyACM0, /dev/ttyUSB0)

### Assembly Errors

1. **Invalid instruction**: Verify mnemonic spelling matches the instruction set
2. **Payload errors**: Ensure payload is 2 digits (00-15) for instructions
3. **Memory full**: Reduce program to 16 instructions or fewer

### Build Issues

1. **Java version mismatch**: Ensure Java 21 is installed and JAVA_HOME is set
2. **Maven errors**: Try using the included Maven wrapper (`./mvnw`)

## Development

### Running Tests

```bash
./mvnw test
```

### Code Structure

- **Assembler**: Core conversion logic from assembly to binary
- **CodeLookUp**: Instruction set definition and mapping
- **InstructionFormatter**: Binary string formatting for serial transmission
- **SerialPortCom**: Hardware communication layer
- **ProgramController**: REST API endpoint

## Future Enhancements

- [ ] Web-based IDE for assembly programming
- [ ] Syntax highlighting and validation
- [ ] Program simulation/debugging
- [ ] Support for labels and symbolic addresses
- [ ] Configurable serial port via application.properties
- [ ] Program readback/verification
- [ ] Multiple memory bank support

