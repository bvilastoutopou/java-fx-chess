package com.example.chess;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Stack;

public class ChessBoard {
    private final int SIZE = 8;
    private final Piece[][] chessBoard = new Piece[SIZE][SIZE];
    private SquarePair lastMove = null;
    private SquarePair lastMoveOrigin = null;
    private int halfMoveCounter;
    private HashMap<String,Integer> repetitionTable = new HashMap<>();
    private int fullMoveCounter;
    Stack<String> undoStack = new Stack<>();
    Stack<String> redoStack = new Stack<>();
    public HashMap<String, Integer> getRepetitionTable() {
        return repetitionTable;
    }
    Stack<String> repetitionUndoStack = new Stack<>();
    Stack<String> repetitionRedoStack = new Stack<>();
    public SquarePair getLastMoveOrigin() {
        return lastMoveOrigin;
    }

    public void setLastMoveOrigin(SquarePair lastMoveOrigin) {
        this.lastMoveOrigin = lastMoveOrigin;
    }

    public void setRepetitionTable(HashMap<String, Integer> repetitionTable) {
        this.repetitionTable = repetitionTable;
    }

    public ChessBoard() throws FileNotFoundException {
        lastMove = null;
        halfMoveCounter = 0;
        fullMoveCounter = 1;
        setChessBoard();
    }

    public String getPositionKey(boolean whitePlays){
        StringBuilder key = new StringBuilder();

        for(int row=0;row<SIZE;row++){
            int empty =0;
            for(int col=0;col<SIZE;col++){
                Piece piece = getPiece(new SquarePair(row,col));
                if(piece==null)empty++;
                else{
                    if(empty>0){
                        key.append(empty);
                        empty=0;
                    }
                    key.append(pieceToChar(piece));
                }
            }
            if(empty>0)key.append(empty);
            if(row<7)key.append('/');
        }
        if(whitePlays){
            key.append(" w ");
        }
        else{
            key.append(" b ");
        }

        Piece whiteKing = getPiece(new SquarePair(7,4));
        Piece blackKing = getPiece(new SquarePair(0,4));

        Piece leftWhiteRook = getPiece(new SquarePair(7,0));
        Piece rightWhiteRook = getPiece(new SquarePair(7,7));

        Piece leftBlackRook = getPiece(new SquarePair(0,0));
        Piece rightBlackRook = getPiece(new SquarePair(0,7));

        String castling = "";

        if(whiteKing!=null){
            if(whiteKing.getPieceType().equals("king") && whiteKing.getColor().equals("white")){
                if(whiteKing.movesDone==0){
                    if(rightWhiteRook!=null){
                        if(rightWhiteRook.getPieceType().equals("rook") && rightWhiteRook.getColor().equals("white")){
                            if(rightWhiteRook.movesDone==0)castling+="K";
                        }
                    }
                    if(leftWhiteRook!=null){
                        if(leftWhiteRook.getPieceType().equals("rook") && leftWhiteRook.getColor().equals("white")){
                            if(leftWhiteRook.movesDone==0)castling+="Q";
                        }
                    }

                }
            }
        }

        if(blackKing!=null){
            if(blackKing.getPieceType().equals("king") && blackKing.getColor().equals("black")){
                if(blackKing.movesDone==0){
                    if(rightBlackRook!=null){
                        if(rightBlackRook.getPieceType().equals("rook") && rightBlackRook.getColor().equals("black")){
                            if(rightBlackRook.movesDone==0)castling+="k";
                        }
                    }
                    if(leftBlackRook!=null){
                        if(leftBlackRook.getPieceType().equals("rook") && leftBlackRook.getColor().equals("black")){
                            if(leftBlackRook.movesDone==0)castling+="q";
                        }
                    }

                }
            }
        }

        if(castling.isEmpty()){
            key.append("-");
        }
        else{
            key.append(castling);
        }
        key.append(" ");
        boolean enPassable = false;
        Piece enPassant = null;
        if(lastMove!=null) {
            enPassant = getPiece(lastMove);
        }

        if (enPassant!=null){
            if(enPassant.getPieceType().equals("pawn")){
                if(enPassant.movesDone==1){
                    if(enPassant.getColor().equals("white")){
                        if(lastMove.getRow()==4){
                            key.append((char)('a' +lastMove.getCol()));
                            key.append(3);
                            enPassable = true;
                        }
                    }else{
                        if(lastMove.getRow()==3){
                            key.append((char)('a' +lastMove.getCol()));
                            key.append(6);
                            enPassable = true;
                        }
                    }
                }
            }
        }

        if(!enPassable){
            key.append("-");
        }

        return key.toString();
    }

    public String fenGenerator(boolean whitePlays){
        String fen = getPositionKey(whitePlays);
        fen += " " + halfMoveCounter + " " + fullMoveCounter;
        if(lastMove != null){
            fen += " " + (char)('a' + lastMove.getCol()) + (lastMove.getRow());
            fen += " " + (char)('a' + lastMoveOrigin.getCol()) + (lastMoveOrigin.getRow());
        } else {
            fen += " -";
        }

        return fen;
    }

    public String validFenGenerator(boolean whitePlays){
        String fen = getPositionKey(whitePlays);
        fen += " " + halfMoveCounter + " " + fullMoveCounter;
        return fen;
    }

    public void fenLoader(String fen) throws FileNotFoundException {
        String[] tokens = fen.split(" ");
        if(tokens.length < 6) throw new IllegalArgumentException("Invalid FEN string");

        String boardPart = tokens[0];
        String turn = tokens[1];
        String castling = tokens[2];
        String enPassantField = tokens[3];
        int halfMove = Integer.parseInt(tokens[4]);
        int fullMove = Integer.parseInt(tokens[5]);
        String lastMoveField = tokens.length > 6 ? tokens[6] : "-";
        String lastMoveOriginField = tokens.length > 7 ? tokens[7] : "-";


        for(int row=0; row<SIZE; row++){
            for(int col=0; col<SIZE; col++){
                chessBoard[row][col] = null;
            }
        }

        String[] rows = boardPart.split("/");
        for(int row=0; row<SIZE; row++){
            String rowStr = rows[row];
            int col = 0;
            for(char c : rowStr.toCharArray()){
                if(Character.isDigit(c)){
                    col += c - '0';
                } else {
                    SquarePair pos = new SquarePair(row, col);
                    Piece piece = charToPiece(c, pos);
                    setPiece(piece, pos);
                    col++;
                }
            }
        }

        boolean whitePlays = turn.equals("w");


        Piece whiteKing = getPiece(new SquarePair(7,4));
        Piece blackKing = getPiece(new SquarePair(0,4));


        if(whiteKing != null && whiteKing.getPieceType().equals("king")) whiteKing.movesDone = castling.contains("K") || castling.contains("Q") ? 0 : 1;
        if(blackKing != null && blackKing.getPieceType().equals("king")) blackKing.movesDone = castling.contains("k") || castling.contains("q") ? 0 : 1;

        Piece rightWhiteRook = getPiece(new SquarePair(7,7));
        Piece leftWhiteRook = getPiece(new SquarePair(7,0));
        Piece rightBlackRook = getPiece(new SquarePair(0,7));
        Piece leftBlackRook = getPiece(new SquarePair(0,0));

        if(rightWhiteRook != null && rightWhiteRook.getPieceType().equals("rook")) rightWhiteRook.movesDone = castling.contains("K") ? 0 : 1;
        if(leftWhiteRook != null && leftWhiteRook.getPieceType().equals("rook")) leftWhiteRook.movesDone = castling.contains("Q") ? 0 : 1;
        if(rightBlackRook != null && rightBlackRook.getPieceType().equals("rook")) rightBlackRook.movesDone = castling.contains("k") ? 0 : 1;
        if(leftBlackRook != null && leftBlackRook.getPieceType().equals("rook")) leftBlackRook.movesDone = castling.contains("q") ? 0 : 1;

        if(!lastMoveField.equals("-")){
            int col = lastMoveField.charAt(0) - 'a';
            int row = Character.getNumericValue(lastMoveField.charAt(1));
            lastMove = new SquarePair(row, col);
        }
        if(!lastMoveOriginField.equals("-")){
            int col = lastMoveOriginField.charAt(0) - 'a';
            int row = Character.getNumericValue(lastMoveOriginField.charAt(1));
            lastMoveOrigin = new SquarePair(row, col);
        }

        if(!enPassantField.equals("-")) {
            Piece p = getPiece(lastMove);
            p.movesDone = 1;
        }

        halfMoveCounter = halfMove;
        fullMoveCounter = fullMove;

        for(int col=0;col<SIZE;col++){
            Piece p = getPiece(new SquarePair(2,col));
            if(p!=null && p.getPieceType().equals("pawn")){
                p.setMovesDone(1);
            }
        }
        for(int col=0;col<SIZE;col++){
            Piece p = getPiece(new SquarePair(5,col));
            if(p!=null && p.getPieceType().equals("pawn")){
                p.setMovesDone(1);
            }
        }

    }


    private char pieceToChar(Piece piece){
        char c = switch(piece.getPieceType()){
            case "king" -> 'k';
            case "queen" -> 'q';
            case "rook" -> 'r';
            case "bishop" -> 'b';
            case "knight" -> 'n';
            case "pawn" -> 'p';
            default -> '?';
        };
        if(piece.getColor().equals("white")){
            return Character.toUpperCase(c);
        }else{
            return c;
        }
    }

    private Piece charToPiece(char c, SquarePair position) throws FileNotFoundException {
        boolean white = Character.isUpperCase(c);
        char lower = Character.toLowerCase(c);
        return switch(lower) {
            case 'k' -> white ? new WhiteKing(position) : new BlackKing(position);
            case 'q' -> white ? new WhiteQueen(position) : new BlackQueen(position);
            case 'r' -> white ? new WhiteRook(position) : new BlackRook(position);
            case 'b' -> white ? new WhiteBishop(position) : new BlackBishop(position);
            case 'n' -> white ? new WhiteKnight(position) : new BlackKnight(position);
            case 'p' -> white ? new WhitePawn(position) : new BlackPawn(position);
            default -> null;
        };
    }

    public boolean determineInsufficientMaterial(){
        int pieceCounter = 0;
        int whiteBishopCounter = 0;
        int whiteKnightCounter = 0;
        int blackBishopCounter = 0;
        int blackKnightCounter = 0;
        SquarePair whiteBishopPos = null;
        SquarePair blackBishopPos = null;
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                Piece piece = getPiece(new SquarePair(row,col));
                if(piece!=null){
                    pieceCounter++;
                    if(piece.getPieceType().equals("pawn") || piece.getPieceType().equals("queen") || piece.getPieceType().equals("rook")){
                        return false;
                    }
                    else if(piece.getPieceType().equals("bishop")){
                        if(piece.getColor().equals("white")){
                            whiteBishopCounter++;
                            whiteBishopPos = new SquarePair(row,col);
                        }
                        else{
                            blackBishopCounter++;
                            blackBishopPos = new SquarePair(row,col);
                        }
                    }
                    else if(piece.getPieceType().equals("knight")){
                        if(piece.getColor().equals("white")){
                            whiteKnightCounter++;
                        }
                        else{
                            blackKnightCounter++;
                        }
                    }
                }
            }
        }
        if(pieceCounter==2){
            return true;
        }else if(pieceCounter==3){
            return whiteBishopCounter == 1 || blackBishopCounter == 1 || blackKnightCounter == 1 || whiteKnightCounter == 1;
        }else if(pieceCounter==4){
            if(whiteBishopCounter==1 && blackBishopCounter==1){
                boolean isLightWhite = (whiteBishopPos.getRow()+whiteBishopPos.getCol())%2 == 0;
                boolean isLightBlack = (blackBishopPos.getRow()+blackBishopPos.getCol())%2 == 0;
                return isLightBlack == isLightWhite;
            }
        }
        return false;
    }

    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public void incrementHalfMoveCounter(){
        halfMoveCounter++;
    }

    public void resetHalfMoveCounter(){
        halfMoveCounter = 0;
    }

    public int getFullMoveCounter() {
        return fullMoveCounter;
    }

    public void incrementFullMoveCounter(){
        fullMoveCounter++;
    }

    public boolean findAllMoves(String color) throws FileNotFoundException {
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++) {
                SquarePair pair = new SquarePair(row,col);
                Piece piece = getPiece(pair);
                if(piece!=null && piece.getColor().equals(color)){
                    piece.findAllowedMoves(this);
                    if(!piece.allowedMoves.isEmpty() || !piece.allowedSpecialMoves.isEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public SquarePair findKing(String color){
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                SquarePair pair = new SquarePair(row,col);
                if(getPiece(pair)!=null){
                    if(getPiece(pair).getColor().equals(color) && getPiece(pair).getPieceType().equals("king")){
                        return pair;
                    }
                }
            }
        }
        return null;
    }

    public SquarePair getLastMove() {
        return lastMove;
    }

    public void setLastMove(SquarePair lastMove) {
        this.lastMove = lastMove;
    }

    public void setPiece(Piece piece, SquarePair pair){
        int row = pair.getRow();
        int col = pair.getCol();
        chessBoard[row][col] = piece;
    }

    public Piece getPiece(SquarePair pair){
        int row = pair.getRow();
        int col = pair.getCol();
        return chessBoard[row][col];
    }

    public void setChessBoard() throws FileNotFoundException {
        String savedFEN = SettingsManager.get("FEN");
        if (savedFEN != null && !savedFEN.isEmpty()) {
            fenLoader(savedFEN);
            return;
        }
        SquarePair whiteRookLeftPos = new SquarePair(7,0);
        WhiteRook whiteRookLeft = new WhiteRook(whiteRookLeftPos);
        setPiece(whiteRookLeft,whiteRookLeftPos);
        SquarePair whiteRookRightPos = new SquarePair(7,7);
        WhiteRook whiteRookRight = new WhiteRook(whiteRookRightPos);
        setPiece(whiteRookRight,whiteRookRightPos);

        SquarePair blackRookLeftPos = new SquarePair(0,0);
        BlackRook blackRookLeft = new BlackRook(blackRookLeftPos);
        setPiece(blackRookLeft,blackRookLeftPos);
        SquarePair blackRookRightPos = new SquarePair(0,7);
        BlackRook blackRookRight = new BlackRook(blackRookRightPos);
        setPiece(blackRookRight,blackRookRightPos);

        SquarePair whiteBishopLeftPos = new SquarePair(7,2);
        WhiteBishop whiteBishopLeft = new WhiteBishop(whiteBishopLeftPos);
        setPiece(whiteBishopLeft,whiteBishopLeftPos);
        SquarePair whiteBishopRightPos = new SquarePair(7,5);
        WhiteBishop whiteBishopRight = new WhiteBishop(whiteBishopRightPos);
        setPiece(whiteBishopRight,whiteBishopRightPos);

        SquarePair blackBishopLeftPos = new SquarePair(0,2);
        BlackBishop blackBishopLeft = new BlackBishop(blackBishopLeftPos);
        setPiece(blackBishopLeft,blackBishopLeftPos);
        SquarePair blackBishopRightPos = new SquarePair(0,5);
        BlackBishop blackBishopRight = new BlackBishop(blackBishopRightPos);
        setPiece(blackBishopRight,blackBishopRightPos);

        SquarePair whiteQueenPos = new SquarePair(7,3);
        WhiteQueen whiteQueen = new WhiteQueen(whiteQueenPos);
        setPiece(whiteQueen,whiteQueenPos);

        SquarePair blackQueenPos = new SquarePair(0,3);
        BlackQueen blackQueen = new BlackQueen(blackQueenPos);
        setPiece(blackQueen,blackQueenPos);

        SquarePair whiteKnightLeftPos = new SquarePair(7,1);
        WhiteKnight whiteKnightLeft = new WhiteKnight(whiteKnightLeftPos);
        setPiece(whiteKnightLeft,whiteKnightLeftPos);
        SquarePair whiteKnightRightPos = new SquarePair(7,6);
        WhiteKnight whiteKnightRight = new WhiteKnight(whiteKnightRightPos);
        setPiece(whiteKnightRight,whiteKnightRightPos);

        SquarePair blackKnightLeftPos = new SquarePair(0,1);
        BlackKnight blackKnightLeft = new BlackKnight(blackKnightLeftPos);
        setPiece(blackKnightLeft,blackKnightLeftPos);
        SquarePair blackKnightRightPos = new SquarePair(0,6);
        BlackKnight blackKnightRight = new BlackKnight(blackKnightRightPos);
        setPiece(blackKnightRight,blackKnightRightPos);

        for(int i=0;i<SIZE;i++){
            SquarePair whitePawnPos = new SquarePair(6,i);
            WhitePawn whitePawn = new WhitePawn(whitePawnPos);
            setPiece(whitePawn,whitePawnPos);
        }

        for(int i=0;i<SIZE;i++){
            SquarePair blackPawnPos = new SquarePair(1,i);
            BlackPawn blackPawn = new BlackPawn(blackPawnPos);
            setPiece(blackPawn,blackPawnPos);
        }

        SquarePair whiteKingPos = new SquarePair(7,4);
        WhiteKing whiteKing = new WhiteKing(whiteKingPos);
        setPiece(whiteKing,whiteKingPos);

        SquarePair blackKingPos = new SquarePair(0,4);
        BlackKing blackKing = new BlackKing(blackKingPos);
        setPiece(blackKing,blackKingPos);
    }

}
