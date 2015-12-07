package com.example.juanv.a14juanms_2avf;

/**
 * Created by juanv on 05/12/2015.
 */
public class Salario {
    private String month;
    private int total_salary;

    public Salario(){

    }

    public Salario(String month, int total_salary){
       this.month=month;
        this.total_salary=total_salary;
    }

    public String getMonth(){
        return this.month;
    }
    public int getTotal_salary(){
        return this.total_salary;
    }
    public void setMonth(String month){
        this.month=month;
    }
    public void setTotal_salary(int total_salary){
        this.total_salary=total_salary;
    }

    @Override
    public String toString(){
        return "Mes: "+this.month+" Salario total="+this.total_salary;
    }
}
