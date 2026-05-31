package com.example.__Bit_Microcontrolller_Assembler.service;

import org.springframework.stereotype.Service;

@Service
public class InstructionFormatter {

    public String formatInstruction(String[] instructions){
        String formattedInstructions = "";

        for(int i = 0; i < instructions.length; i++){
            formattedInstructions += instructions[i];
            if(i < instructions.length-1) {
                formattedInstructions += ",";
            }
        }
        formattedInstructions += "\n";
        System.out.println(formattedInstructions);
        return formattedInstructions;
    }
}
