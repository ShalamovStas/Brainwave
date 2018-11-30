package com.example.shalamov.brainwave.utils;

public class ToolForNotepad {

    private int numberOfSentences; // количество предложений в уроке

    // внутренние переменные
    // для добавления предложений в отображаемый список по очереди
    // для быстроты работы
    private int step; // шаг, количество элементов, которые добавляются за один раз
    private int currentPosition; //
    private int startElementIndex;
    private int stopElementIndex;


    public ToolForNotepad(){
        setStartPosition();
    }

    public void setNumberOfSentences(int numberOfSentences) {
        this.numberOfSentences = numberOfSentences;
    }

    public void setStartPosition(){
        currentPosition = 0;
        step = 10;
        startElementIndex = 0;
        stopElementIndex = 10;
    }
    //метод возвращает индекс первого элемента и индекс последнего элемента
    // для одного шага
    //
    // ---------i=0--------  первый элемент
    // ---------i=1--------
    // ---------...--------
    // ---------i=10-------  последний элемент
    //
    public Integer[] getIndexes(){
        Integer[] indexes = new Integer[2];
        if(currentPosition != 0){
            startElementIndex = stopElementIndex;
            stopElementIndex = stopElementIndex + step;
        }
        if(stopElementIndex > numberOfSentences){
            stopElementIndex = numberOfSentences;
        }

        if(startElementIndex > numberOfSentences){
            startElementIndex = numberOfSentences;
        }
        indexes[0] = startElementIndex;
        indexes[1] = stopElementIndex;
        currentPosition++;

        return indexes;
    }
}
