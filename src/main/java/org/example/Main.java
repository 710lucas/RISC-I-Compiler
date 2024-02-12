package org.example;

import java.io.*;
import java.util.*;

public class Main {

    static Map<String, Integer> opcodes = new HashMap<>(){{
        put("ADD", 0x01);
        put("ADDC", 0x02);
        put("SUB", 0x03);
        put("SUBC", 0x04);
        put("AND", 0x06);
        put("OR", 0x07);
        put("XOR", 0x08);
        put("SLL", 0x09);
        put("SRL", 0x0A);
        put("SRA", 0x0B);
        put("LDL", 0x0C);
        put("STL", 0x10);
        put("JMP", 0x13);
        put("JMPR", 0x14);
        put("CALL", 0x15);
        put("RET", 0x17);
        put("PRNT", 0x18);
    }};

    public static String lerArquivo(String nomeArquivo){
        String out = "";
        try{
            FileReader arquivo = new FileReader(nomeArquivo);
            BufferedReader leitor = new BufferedReader(arquivo);

            String linha;
            while((linha = leitor.readLine()) != null)
                out+=linha+"\n";
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] compilar(String arquivo){
        List<Integer> ints = new ArrayList<>();

        String[] linhas = arquivo.split("\n");
        for(String linha : linhas){

            if(linha.contains("//"))
                continue;

            System.out.println("Linha sendo processada: "+linha);
            String[] elementos = linha.split("\\s+");
            if(elementos.length != 4)
                return null;
            String opcodeString = elementos[0];
            int op1 = Integer.parseInt(elementos[1].substring(2), 16);
            int op2 = Integer.parseInt(elementos[2].substring(2), 16);
            int op3 = Integer.parseInt(elementos[3].substring(2), 16);
            int opcode = opcodes.get(opcodeString);
            if(opcodes.get(opcodeString) == null)
                return null;
            ints.add(opcode);
            ints.add(op1);
            ints.add(op2);
            ints.add(op3);

            System.out.println("Linha resultante: "+Integer.toHexString(opcode)+" "+
                    Integer.toHexString(op1)+ " "+
                    Integer.toHexString(op2) + " "+
                    Integer.toHexString(op3));
        }
        byte bytes[] = new byte[(ints.size()/4)*6];
        int j = 0; //intAtual
        for(int i = 0; i<bytes.length; i+=6){
            bytes[i] = (byte)(ints.get(j) & 0xFF);

            bytes[i+1] = (byte)((ints.get(j+1)>>8)&0xFF);
            bytes[i+2] = (byte)((ints.get(j+1))&0xFF);

            bytes[i+3] = (byte)((ints.get(j+2)>>8)&0xFF);
            bytes[i+4] = (byte)((ints.get(j+2))&0xFF);

            bytes[i+5] = (byte)((ints.get(j+3))&0xFF);
            j+=4;
            System.out.println("0x"+Integer.toHexString(bytes[i])+
                    Integer.toHexString(bytes[i+1])+
                    Integer.toHexString(bytes[i+2])+
                    Integer.toHexString(bytes[i+3])+
                    Integer.toHexString(bytes[i+4])+
                    Integer.toHexString(bytes[i+5]));
        }
        for(byte b : bytes){
            System.out.println("b: "+Integer.toHexString(b));
        }
        return bytes;
    }

    public static void main(String[] args) {

        String nomeArquivo;

        Scanner sc = new Scanner(System.in);

        if(args.length != 1) {
            System.out.print("Informe o nome do arquivo: ");
            nomeArquivo = sc.nextLine();
        }

        else
            nomeArquivo = args[0];

        String arquivo = lerArquivo(nomeArquivo);
        try (FileOutputStream f = new FileOutputStream("a.bin")){
            byte[] bytesToWrite = compilar(arquivo);
            if(bytesToWrite != null)
                f.write(bytesToWrite);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}