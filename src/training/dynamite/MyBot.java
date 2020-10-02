package training.dynamite;

import com.softwire.dynamite.bot.Bot;
import com.softwire.dynamite.game.*;

public class MyBot implements Bot {

    private int dynamiteCount;
    private int roundCount;

    public MyBot() {
    }

    @Override
    public Move makeMove(Gamestate gamestate) {
        roundCount++;
        if (roundCount < 1000) {

            if (gamestate.getRounds().size() == 0) {
                return Move.W;
            }

            if (gamestate.getRounds().size() == 1) {
                return moveDynamite(gamestate);
            }

            if (enemyLastXMovesAreSame(gamestate, 2)) {
                return moveBeatPrevious(gamestate);
            }

            if (gamestate.getRounds().size() % 15 == 0) {
                return moveDynamite(gamestate);
            }

            if (dynamiteCount < 100 && gamestate.getRounds().size() > 950) {
                return moveDynamite(gamestate);
            }
        }
        return moveRandom();
    }

    private Move moveDynamite(Gamestate gamestate) {
        if (dynamiteCount < 100) {
            dynamiteCount++;
            return Move.D;
        }
        return moveRandom();
    }

    private Move moveRandom() {
        int randomNumberBetween0And3 = (int) Math.floor(Math.random() * 3.0D);
        Move[] possibleMoves = new Move[]{Move.R, Move.P, Move.S};
        return possibleMoves[randomNumberBetween0And3];
    }

    private boolean enemyLastXMovesAreSame(Gamestate gamestate, int moves) {
        int sameMoveCount = 0;
        int round = gamestate.getRounds().size();
        int previousXRound = round - moves;

        if (round > moves) {
            for (int i = previousXRound; i < round - 1; i++) {
                if (gamestate.getRounds().get(i).getP2() == gamestate.getRounds().get(i + 1).getP2()) {
                    sameMoveCount++;
                }
            }
        }
        return sameMoveCount >= moves;
    }

    private Move moveBeatPrevious(Gamestate gamestate) {
        Move enemyLastMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP2();

        switch (enemyLastMove) {
            case D:
                return Move.W;
            case W:
                return Move.W;
            case R:
                return Move.P;
            case P:
                return Move.S;
            case S:
                return Move.R;
        }
        return Move.R;
    }

    private void printStats(Gamestate gamestate) {
        if (gamestate.getRounds().size() > 1) {
            Move myMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP1();
            Move enemyMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP2();
            System.out.println("Dynamite Count: " + dynamiteCount + "   Round: " + gamestate.getRounds().size() +
                    "    Me:" + myMove + "    Enemy:" + enemyMove);
        }
    }
}
