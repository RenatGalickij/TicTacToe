package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import tictactoe.AdversarialSearchTicTacToe;
import tictactoe.State;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Random random = new Random();

    ArrayList<Button> buttons;

    AdversarialSearchTicTacToe ticTacToeAI = new AdversarialSearchTicTacToe();

    boolean isPvP = false;
    boolean isPlayerOneTurn = true;

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    @FXML
    private Text winnerText;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));
        winnerText.setText("Greetings");
        buttons.forEach(button -> {
            setupButton(button);
            button.setFocusTraversable(false);
        });
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
        if (isPvP == true) {
            isPlayerOneTurn = true;
        } else {
            makeAIMove();
        }
    }


    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            if (isPvP == true) {
                if (isPlayerOneTurn) {
                    button.setText("O");
                } else {
                    button.setText("X");
                }
                isPlayerOneTurn = !isPlayerOneTurn;
            } else {
                button.setText("O");
                button.setDisable(true);
                makeAIMove();
            }
            checkIfGameIsOver();
        });
    }

    public void makeAIMove() {
        if (!isPvP) { // если не установлен режим PvP
            for (Button button : buttons) {
                button.setDisable(true); // блокируем все кнопки
            }
            int move = ticTacToeAI.minMaxDecision(getBoardState());
            pickButton(move);
            checkIfGameIsOver();
            for (Button button : buttons) {
                button.setDisable(false); // разблокируем все кнопки
            }
        }
//        int move = ticTacToeAI.minMaxDecision(getBoardState());
//        pickButton(move);
    }

    private void pickButton(int index) {
        buttons.get(index).setText("X");
        buttons.get(index).setDisable(true);
    }

    public State getBoardState() {
        String[] board = new String[9];

        for (int i = 0; i < buttons.size(); i++) {
            board[i] = buttons.get(i).getText();
        }

        return new State(0, board);
    }

    public void checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;

            };


            if (isPvP == true) {
                if (line.equals("XXX")) {
                    winnerText.setText("Player 1 Wins!");

                } else if (line.equals("OOO")) {
                    winnerText.setText("Player 2 Wins!");

                }
            } else {
                if (line.equals("XXX")) {
                    winnerText.setText("AI won!");
                }

                //O winner
                else if (line.equals("OOO")) {
                    winnerText.setText("You won!");
                }
            }
        }
        if(checkIfTie()) {
            winnerText.setText("Draw");
        }
    }

    public boolean checkIfTie() {
        for (Button button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    @FXML
    public void setPvC(ActionEvent actionEvent) {
        buttons.forEach(this::resetButton);
        buttons.forEach(button -> button.setDisable(false));
        isPvP = false;
        makeAIMove();
    }

    @FXML
    public void setPvP(ActionEvent actionEvent) {
        buttons.forEach(this::resetButton);
        buttons.forEach(button -> button.setDisable(false));
        isPvP = true;
    }
}

