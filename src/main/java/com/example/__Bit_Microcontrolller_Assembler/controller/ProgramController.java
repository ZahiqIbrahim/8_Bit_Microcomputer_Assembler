package com.example.__Bit_Microcontrolller_Assembler.controller;


import com.example.__Bit_Microcontrolller_Assembler.service.Assembler;
import com.example.__Bit_Microcontrolller_Assembler.service.SerialPortCom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProgramController {

    @Autowired
    private Assembler assembler;
    @Autowired
    private SerialPortCom serialPortCom;

    @PostMapping("/new-program")
    public ResponseEntity<?> addProgram(@RequestBody String program){
        try {
            serialPortCom.sendInstructions(assembler.convertProgram(program));
            return ResponseEntity.ok("Program uploaded successfully.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("Error ", e.getMessage()));
        }
    }
}
