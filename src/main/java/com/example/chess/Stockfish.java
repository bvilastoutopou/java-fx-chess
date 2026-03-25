package com.example.chess;

import java.io.*;

public class Stockfish {

    private Process engineProcess;
    private BufferedReader processReader;
    private BufferedWriter processWriter;
    private final String enginePath;

    public Stockfish(String enginePath) {
        this.enginePath = enginePath;
    }

    public boolean startEngine() {
        try {
            engineProcess = new ProcessBuilder(enginePath).redirectErrorStream(true).start();
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));

            sendCommand("uci");
            waitFor("uciok");
            sendCommand("isready");
            waitFor("readyok");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stopEngine() {
        sendCommand("quit");
        try {
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        engineProcess.destroy();
    }

    private void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitFor(String expected) {
        try {
            String line;
            while ((line = processReader.readLine()) != null) {
                if (line.contains(expected)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns Stockfish best move as a string like "e2e4".
     * This can be used directly in your existing controller:
     * chessBoard.getPiece(origin).moveAi(chessBoard, destination, squares);
     */
    public String getBestMove(String fen, int elo, int movetimeMs) {
        sendCommand("uci");
        sendCommand("isready");
        waitFor("readyok");

        sendCommand("setoption name UCI_LimitStrength value true");
        sendCommand("setoption name UCI_Elo value " + elo);

        sendCommand("position fen " + fen);
        sendCommand("go movetime " + movetimeMs);

        String line;
        try {
            while ((line = processReader.readLine()) != null) {
                if (line.startsWith("bestmove")) {
                    return line.split(" ")[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
