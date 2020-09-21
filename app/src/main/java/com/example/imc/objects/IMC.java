package com.example.imc.objects;

import java.text.DecimalFormat;

public class IMC {
        public static String calcIMC(double weight, double height) {
            DecimalFormat df2 = new DecimalFormat("##.00");
            String imc = df2.format(weight / (height * height));
            if (imc.contains(",")){
                return imc.substring(0, imc.indexOf(",")) + "." + imc.substring(imc.indexOf(",") + 1);
            }
            return imc;
        }
        public static String refIMC(String imc, int age, double height){
            DecimalFormat form = new DecimalFormat("###.0");
            double imc_d = Double.parseDouble(imc);
            StringBuilder buffer = new StringBuilder();
            if(age < 15)
                buffer.append("You are less than 15 years.\nSorry you can´t calculate your IMC!!");
            if(age <= 24){
                if(imc_d < 19){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");
                }
                else if(imc_d >= 19 && imc_d < 24){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                }
                else if(imc_d >= 24 && imc_d < 29){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                }
                else if(imc_d >= 29 && imc_d < 39){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                }
                else if(imc_d >= 39){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 19)).append("kg and ").append(form.format(height * height * 24)).append("kg.\n");
            }
            if(age >= 25 && age <= 34){
                if(imc_d < 20){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");            }
                else if(imc_d >= 20 && imc_d < 25){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                }
                else if(imc_d >= 25 && imc_d < 30){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                }
                else if(imc_d >= 30 && imc_d < 40){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                }
                else if(imc_d >= 40){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 20)).append("kg and ").append(form.format(height * height * 25)).append("kg.\n");
            }
            if(age >= 35 && age <= 44) {
                if (imc_d < 21) {
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");
                } else if (imc_d >= 21 && imc_d < 26) {
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                } else if (imc_d >= 26 && imc_d < 31) {
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                } else if (imc_d >= 31 && imc_d < 41) {
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                } else if (imc_d >= 41) {
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 21)).append("kg and ").append(form.format(height * height * 26)).append("kg.\n");
            }
            if(age >= 45 && age <= 54){
                if(imc_d < 22){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");
                }
                else if(imc_d >= 22 && imc_d < 27){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                }
                else if(imc_d >= 27 && imc_d < 32){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                }
                else if(imc_d >= 32 && imc_d < 42){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                }
                else if(imc_d >= 42){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 22)).append("kg and ").append(form.format(height * height * 27)).append("kg.\n");
            }
            if(age >= 55 && age <= 64){
                if(imc_d < 23){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");
                }
                else if(imc_d >= 23 && imc_d < 28){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                }
                else if(imc_d >= 28 && imc_d < 33){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                }
                else if(imc_d >= 33 && imc_d < 43){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                }
                else if(imc_d >= 43){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 23)).append("kg and ").append(form.format(height * height * 28)).append("kg.\n");
            }
            if(age >= 65){
                if(imc_d < 24){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in low weight.\n");
                }
                else if(imc_d >= 24 && imc_d < 29){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in normal weight.\n");
                }
                else if(imc_d >= 29 && imc_d < 34){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in above weight.\n");
                }
                else if(imc_d >= 34 && imc_d < 44){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in obesity.\n");
                }
                else if(imc_d >= 44){
                    buffer.append("Your IMC is ").append(imc_d).append(", you are in serious obesity.\n");
                }
                buffer.append("Your normal weight is between ").append(form.format(height * height * 24)).append("kg and ").append(form.format(height * height * 29)).append("kg.\n");
            }
            return buffer.toString();
        }
}
