package com.sony.tambola;

public interface Game {

    public  boolean checkTopRowWinner(Player player);

    public  boolean checkFullHouse(Player player);

    public  boolean checkFirstFiveNumbers(Player player);
}
