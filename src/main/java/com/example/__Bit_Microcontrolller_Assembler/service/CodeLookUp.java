package com.example.__Bit_Microcontrolller_Assembler.service;

import org.springframework.stereotype.Service;

@Service
public class CodeLookUp {

    public String getOpcode(String ins) {

        switch (ins){
            case "LDA":
                return "0001";
            case "ADD":
                return "0010";
            case "SUB":
                return "0011";
            case "STA":
                return "0100";
            case "LDI":
                return "0101";
            case "JMP":
                return "0110";
            case "JPC":
                return "0111";
            case "JPZ":
                return "1000";
            case "OUT":
                return "11100000";
            case "HLT":
                return "11110000";

            default:
                    throw new RuntimeException("Invalid Instruction");


        }
    }

    public String getPayload(String payLoad) {
        switch (payLoad){
            case "00":
                return "0000";
            case "01":
                return "0001";
            case "02":
                return "0010";
            case "03":
                return "0011";
            case "04":
                return "0100";
            case "05":
                return "0101";
            case "06":
                return "0110";
            case "07":
                return "0111";
            case "08":
                return "1000";
            case "09":
                return "1001";
            case "10":
                return "1010";
            case "11":
                return "1011";
            case "12":
                return "1100";
            case "13":
                return "1101";
            case "14":
                return "1110";
            case "15":
                return "1111";

            default:
                throw new RuntimeException("Invalid PayLoad Or PayLoad Out Of Capacity");
        }
    }
}
