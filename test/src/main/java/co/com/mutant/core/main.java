package co.com.mutant.core;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.ArrayList;
import java.util.Arrays;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class main implements RequestHandler<Object, String> {

    private int preguntas = 3;
    private String isMutant = "false";

    @Override
    public String handleRequest(Object input, Context context) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RqIsMutantDto args = gson.fromJson(input.toString(), RqIsMutantDto.class);
        return (validate(args));
    }

    public String validate(RqIsMutantDto args) {
        String[] dna = args.getDna();
        System.out.println("Parametros de entrada {}" + dna);
        List<String[]> tt1 = new ArrayList<String[]>();

        for (String x : dna) {
            tt1.add(x.split(""));
        }
        for (String[] matriz : tt1) {
            System.out.println(Arrays.toString(matriz));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dna.length; i++) {
            for (int j = 0; j < dna[i].length(); j++) {
                validacionHorizontal(dna, tt1, i, j, stringBuilder);
                validacionVertical(dna, tt1, i, j);

                int acumuladorI = i + 1;
                int acumuladorJ = j;
                acumuladorJ = acumuladorJ + 1;
                validacionDiagonalDerecha(dna, tt1, i, j, acumuladorI, acumuladorJ);

            }
        }
        validacionDiagonalIzquierda(dna, tt1);
        return isMutant;
    }

    private void validacionDiagonalIzquierda(String[] dna, List<String[]> tt1) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dna.length&&calcularLimiteVertical(i,dna.length)==true; i++) {

            for (int j = dna[i].length() - 1; j >= 0; j--) {
                int x, acumuladorI = i + 1, acumuladorJ = j;
                acumuladorJ = acumuladorJ - 1;
                for (x = 1; x <= preguntas && acumuladorJ >= 0 && acumuladorI < dna.length && acumuladorJ < dna[i].length(); x++, acumuladorJ = acumuladorJ - 1) {
                    if (tt1.get(i)[j].equals(tt1.get(acumuladorI)[acumuladorJ])) {
                        stringBuilder.append(tt1.get(i)[j]);
                        if (stringBuilder.length() > 0 && String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1)).equals(tt1.get(acumuladorI)[acumuladorJ]) && stringBuilder.length() == preguntas) {
                            isMutant = "true";
                            System.out.println("Evaluación diagonal izquierda {}" + stringBuilder);
                            stringBuilder = new StringBuilder();
                        }
                    } else {
                        stringBuilder = new StringBuilder();
                        break;
                    }
                    acumuladorI++;
                }
            }
            //
        }
    }

    private StringBuilder validacionDiagonalDerecha(String[] dna, List<String[]> tt1, int i, int j, int acumuladorI, int acumuladorJ) {
        int x;
        StringBuilder stringBuilder = new StringBuilder();
        for (x = 1; x <= preguntas && calcularLimiteVertical(i,dna.length)==true && acumuladorI < dna.length && acumuladorJ < dna[i].length(); x++, acumuladorJ = acumuladorJ + 1) {
            if (tt1.get(i)[j].equals(tt1.get(acumuladorI)[acumuladorJ])) {
                stringBuilder.append(tt1.get(i)[j]);
                if (stringBuilder.length() > 0 && String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1)).equals(tt1.get(acumuladorI)[acumuladorJ]) && stringBuilder.length() == preguntas) {

                    isMutant = "true";
                    System.out.println("Evaluación diagonal derecha {}" + stringBuilder);
                    stringBuilder = new StringBuilder();
                }
            } else{ stringBuilder = new StringBuilder(); break;}
            acumuladorI++;
        }
        return stringBuilder;
    }

    public StringBuilder validacionHorizontal(String[] dna, List<String[]> tt1, int i, int j, StringBuilder stringBuilder) {

        if (calcularLimiteHorizontal(j,tt1.get(i).length)==true && j + 1 < dna[i].length() && tt1.get(i)[j].equals(tt1.get(i)[j + 1])) {
            stringBuilder.append(tt1.get(i)[j]);
            if (stringBuilder.length() > 0 && String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1)).equals(tt1.get(i)[j + 1]) && stringBuilder.length() == preguntas) {

                isMutant = "true";
                System.out.println("Evaluación horizontal {}" + stringBuilder);
                stringBuilder = new StringBuilder();

            }
        } else {
            stringBuilder = new StringBuilder();
        }
        return stringBuilder;
    }

    public StringBuilder validacionVertical(String[] dna, List<String[]> tt1, int i, int j) {
        StringBuilder stringBuilder = new StringBuilder();
        int acumuladorI=i;
        for(int x=0; x<preguntas && calcularLimiteVertical(i, dna.length) == true&&x<preguntas; x++, acumuladorI=acumuladorI + 1) {
            if (tt1.get(i)[j].equals(tt1.get(acumuladorI + 1)[j])) {
                stringBuilder.append(tt1.get(acumuladorI)[j]);
                if (stringBuilder.length() > 0 && String.valueOf(stringBuilder.charAt(stringBuilder.length() - 1)).equals(tt1.get(acumuladorI + 1)[j]) && stringBuilder.length() == preguntas) {

                    isMutant = "true";
                    System.out.println("Evaluación vertical {}" + stringBuilder);
                    stringBuilder = new StringBuilder();

                }
            } else {
                stringBuilder = new StringBuilder(); break;
            }
        }
        return stringBuilder;
    }



    public boolean calcularLimiteVertical(int filaActual, int logitudTotal){
        int res = (logitudTotal-1)-filaActual;
        if((logitudTotal-1)-filaActual>preguntas){
            return true;
        }
        return false;
    }

    public boolean calcularLimiteHorizontal(int columna, int logitudTotal){
        int res = (logitudTotal-1)-columna;
        if((logitudTotal-1)-columna>preguntas){
            return true;
        }
        return false;
    }


}
