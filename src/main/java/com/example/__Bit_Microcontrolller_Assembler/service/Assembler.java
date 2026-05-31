package com.example.__Bit_Microcontrolller_Assembler.service;

import org.springframework.stereotype.Service;

@Service
public class Assembler {

    public void convertProgram(String program){

        String[] instructions = program.split("\n");

        for (String ins : instructions) {
            System.out.println(ins);
        }

    }
}
