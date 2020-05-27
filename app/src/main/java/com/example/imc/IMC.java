package com.example.imc;

import java.text.DecimalFormat;

public class IMC {
        public static String calcIMC(double weight, double height) {
            DecimalFormat df2 = new DecimalFormat("#.##");
            String imc = df2.format(weight / (height * height));
            return imc;
        }
        public static String refIMC(String imc, int age, double height){
            Double imc_d = Double.parseDouble(imc);
            StringBuffer buffer = new StringBuffer();
            if(age < 15)
                buffer.append("You are less than 15 years.\nSorry you can´t calculate your IMC!!");
            if(age <= 24){
                if(imc_d < 19){
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
                }
                else if(imc_d >= 19 && imc_d < 24){
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                }
                else if(imc_d >= 24 && imc_d < 29){
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                }
                else if(imc_d >= 29 && imc_d < 39){
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                }
                else if(imc_d >= 39){
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 19 + " and " + height * height * 24 + ".\n");

            }
            if(age >= 25 && age <= 34){
                if(imc_d < 20){
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");            }
                else if(imc_d >= 20 && imc_d < 25){
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                }
                else if(imc_d >= 25 && imc_d < 30){
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                }
                else if(imc_d >= 30 && imc_d < 40){
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                }
                else if(imc_d >= 40){
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 20 + " and " + height * height * 25 + ".\n");
            }
            if(age >= 35 && age <= 44) {
                if (imc_d < 21) {
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
                } else if (imc_d >= 21 && imc_d < 26) {
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                } else if (imc_d >= 26 && imc_d < 31) {
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                } else if (imc_d >= 31 && imc_d < 41) {
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                } else if (imc_d >= 41) {
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 21 + " and " + height * height * 26 + ".\n");
            }
            if(age >= 45 && age <= 54){
                if(imc_d < 22){
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
                }
                else if(imc_d >= 22 && imc_d < 27){
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                }
                else if(imc_d >= 27 && imc_d < 32){
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                }
                else if(imc_d >= 32 && imc_d < 42){
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                }
                else if(imc_d >= 42){
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 22 + " and " + height * height * 27 + ".\n");
            }
            if(age >= 55 && age <= 64){
                if(imc_d < 23){
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
                }
                else if(imc_d >= 23 && imc_d < 28){
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                }
                else if(imc_d >= 28 && imc_d < 33){
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                }
                else if(imc_d >= 33 && imc_d < 43){
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                }
                else if(imc_d >= 43){
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 23 + " and " + height * height * 28 + ".\n");
            }
            if(age >= 65){
                if(imc_d < 24){
                    buffer.append("Your IMC is " + imc_d + ", you are in low weight.\n");
                }
                else if(imc_d >= 24 && imc_d < 29){
                    buffer.append("Your IMC is " + imc_d + ", you are in normal weight.\n");
                }
                else if(imc_d >= 29 && imc_d < 34){
                    buffer.append("Your IMC is " + imc_d + ", you are in above weight.\n");
                }
                else if(imc_d >= 34 && imc_d < 44){
                    buffer.append("Your IMC is " + imc_d + ", you are in obesity.\n");
                }
                else if(imc_d >= 44){
                    buffer.append("Your IMC is " + imc_d + ", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between " + height * height * 24 + " and " + height * height * 29 + ".\n");
            }
            return buffer.toString();
        }
}
