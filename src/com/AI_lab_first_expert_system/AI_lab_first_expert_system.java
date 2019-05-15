package com.AI_lab_first_expert_system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AI_lab_first_expert_system {

    public static void main(String[] args) {
        HashMap<String, String> questions = getQuestions();     //Contains list of questions
        HashMap<String, String> animals = getAnimals();         //Contains list of available animals
        HashMap<String, Vector<String>>  logic = getLogic();    //Contains logic of each animal

        HashMap<String, Boolean> questArr = new HashMap<>();    //Contains answers to the questions
        HashMap<String, Boolean> answArr = new HashMap<>();     //Contains map of animals with Boolean answer true-is that animal/false-not this animal+

        for (Vector<String> s : logic.values()) {
            for (String ss : s) {
                questArr.put(ss, false);
            }
        }
        for (String s : logic.keySet()) {
            answArr.put(s, null);
        }

/*        System.out.println(logic);
        System.out.println(animals);
        System.out.println(answArr);*/
    /*
    pozostaje jeden problem z działaniem tego programu...
    potrzebne są negacje lub 'grupowanie' pytań przy których gdy jedno sie wybierze to pozostałe staja się negacją !!!!
    czyli mnogość pytań i część z nich się pokrywająca z pozostałymi zwierzętami doprowadza do błędnych wniosków przez program T.T
     */


        try {
            while(areSame(answArr)) {
                Random rnd = new Random();
                List<String> keys = new ArrayList<>(questions.keySet());
                String randomKey = keys.get(rnd.nextInt(keys.size()));
                System.out.println(questions.get(randomKey));

                questioning(questions, randomKey, questArr, logic, answArr, animals);

            }

        }catch (IllegalArgumentException iae) {
            System.out.println("No to nie wiem co to jest...");
        }


        for (String k : answArr.keySet()) {
            if (answArr.get(k)) {
                for (String s : logic.keySet()){
                    if(logic.get(s).contains(k)) {
                        for (String j: logic.get(s)) {
                            if(j != s){
                                questioning(questions, j, questArr, logic, answArr, animals);
                                //System.out.println(j);
                            }
                        }

                    }
                }
                System.out.println("To zwierze to... " + animals.get(k));
            }
        }

    }

    private static void questioning(HashMap<String,String> questions, String key, HashMap<String, Boolean> questArr, HashMap<String, Vector<String>>  logic, HashMap<String, Boolean> answArr, HashMap<String, String> animals) {
        Scanner scanner = new Scanner(System.in);
        String tempStr;

        if(animals.keySet().contains(key))
            return;

        if(!questions.keySet().contains(key))
            return;

        System.out.println(questions.get(key) + " " + key);
        tempStr = scanner.nextLine();
        if (tempStr.equals("TAK") || tempStr.equals("tak") || tempStr.equals("T") || tempStr.equals("t")) {
            questArr.replace(key, false, true);
        }
        questions.remove(key);
        calculateAnimalLogic(answArr, questArr, logic);

    }

    private static HashMap<String, String> getQuestions() {
        HashMap<String, String> quest = new HashMap<>();
        String fileName = "questions.txt";

        return getToMap(quest,fileName," - ");
    }

    private static HashMap<String, String> getAnimals() {
        HashMap<String, String> anim = new HashMap<>();
        String fileName = "animals.txt";

        return getToMap(anim, fileName, " - ");
    }

    private static HashMap<String,Vector<String>> getLogic() {
        HashMap<String, Vector<String>> log = new HashMap<>();
        String fileName = "logic.txt";

        String line = null;

        try {
            FileReader fielReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fielReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] ask = line.split(" -> ");
                Vector<String> operands = new Vector<>(Arrays.asList(ask[1].split(",")));
                log.put(ask[0], operands);
            }

            bufferedReader.close();

        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }

        return log;
    }

    private static HashMap<String, String> getToMap(HashMap<String,String> map, String fileName, String splitBy) {
        String line = null;

        try {
            FileReader fielReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fielReader);

            while ((line = bufferedReader.readLine()) != null) {
                String[] ask = line.split(" - ");
                map.put(ask[0], ask[1]);
            }

            bufferedReader.close();

        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }

        return map;
    }

    private static boolean areSame(HashMap<String, Boolean> arr) {
        for(Boolean b : arr.values()) {
            if(b == null) continue;
            if (b) return false;
        }
        return true;
    }

    private static void calculateAnimalLogic(HashMap<String, Boolean> answArr, HashMap<String, Boolean> questArr, HashMap<String, Vector<String>> logic)
    {//3 stany zamiast 2 stanowego bool np 0,1,?/tak,nie,?
/*        for (String k : l.keySet()) {
            Boolean temp = true;
            for(String k2: l.get(k)) {
                if(a.keySet().contains(k2))
                    if(!a.get(k2))
                        temp = false;
                if(!q.get(k2))
                    temp = false;
            }
            if(temp) {
                a.replace(k,false,temp);
            }
        }*/

        for(String k1 : logic.keySet()){
            Boolean temp = true;
            for(String k2 : logic.get(k1)) {
                if(answArr.keySet().contains(k2)){
                    if(answArr.get(k2) != null){
                        if(!answArr.get(k2)) {
                            temp = false;
                            break;
                        }
                    }
                    else {
                        temp = null;
                        break;
                    }
                }
                else if (!questArr.get(k2)){
                    temp = false;
                    break;
                }
            }
            answArr.replace(k1,temp);
        }
    }
}

