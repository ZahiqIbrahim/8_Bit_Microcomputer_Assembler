package com.example.__Bit_Microcontrolller_Assembler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Assembler {

    @Autowired
    private CodeLookUp codeLookUp;

    public void convertProgram(String program){

        String[] instructions = program.split("\\R");

        for (String ins : instructions) {
            System.out.println(ins);
        }

        String[] instructionsOpCode = new String[16];
        if(instructions.length > 16){
            throw new RuntimeException("Memory Capacity Exceeded - Memory is of 16 byte");
        }

        for (int i = 0; i < instructions.length; i++) {
            instructionsOpCode[i] = "";
            String ins = instructions[i].substring(0,3);
            instructionsOpCode[i] += codeLookUp.getOpcode(ins);

            if(!instructionsOpCode[i].equals("11100000") && !instructionsOpCode[i].equals("11110000")) {
                if(instructions[i].length() < 6) {
                   throw new RuntimeException("No payload provided to an instruction that required one");
                }else if(!(instructions[i].length() > 6)){
                    String payLoad = instructions[i].substring(4, 6);
                    instructionsOpCode[i] += codeLookUp.getPayload(payLoad);
                }else{
                    throw new RuntimeException("Invalid PayLoad Or PayLoad Out Of Capacity");
                }
            }else if(instructions[i].length() > 3){
                throw new RuntimeException("OUT or HLT requires no payload");
            }

        }

        for(int i = 0; i < instructionsOpCode.length; i++){

            if(instructionsOpCode[i] == null || instructionsOpCode[i].isEmpty()){
                instructionsOpCode[i] = "00000000";
            }
        }

        for (String ins : instructionsOpCode) {
            System.out.println(ins);
        }


    }
}
