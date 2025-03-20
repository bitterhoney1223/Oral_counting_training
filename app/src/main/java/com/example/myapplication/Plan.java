package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Plan extends AppCompatActivity {

    private TextView problemTextView;
    private EditText inputField;
    private Button sendButton;
    private Button removeButton;
    private Button continueButton;
    private Button finishButton;
    private TextView resultTextView;
    private TextView adviceTextView;
    private TextView timerTextView;

    private int currentDifficulty = 3;
    private String currentOperation;
    private int correctAnswer;
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 30000; // 30 seconds
    private boolean timerRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

    private static final String[] ADDITION_TIPS = {
            "Попробуйте разбить числа на десятки и единицы. Например, 34 + 56 = (30 + 50) + (4 + 6).",
            "Используйте метод округления. Например, 47 + 28 = (50 + 30) - 5.",
            "Тренируйтесь складывать числа в уме, начиная с простых примеров.",
            "Попробуйте визуализировать числа в уме и складывать их по частям.",
            "Используйте ассоциативные свойства сложения: (a + b) + c = a + (b + c)."
    };

    private static final String[] SUBTRACTION_TIPS = {
            "Разбейте числа на десятки и единицы. Например, 56 - 23 = (50 - 20) + (6 - 3).",
            "Используйте метод дополнения. Например, 100 - 37 = 63, так как 37 + 63 = 100.",
            "Попробуйте округлять числа. Например, 72 - 48 = (70 - 50) + 2.",
            "Тренируйтесь вычитать числа в уме, начиная с простых примеров.",
            "Используйте визуализацию: представьте, что вы отнимаете одно число от другого."
    };

    private static final String[] MULTIPLICATION_TIPS = {
            "Используйте разложение на множители. Например, 12 * 15 = (10 * 15) + (2 * 15).",
            "Попробуйте округлять числа. Например, 19 * 21 = (20 * 21) - 21.",
            "Тренируйте таблицу умножения, чтобы быстрее решать примеры.",
            "Используйте метод распределения: a * (b + c) = (a * b) + (a * c).",
            "Попробуйте визуализировать умножение как повторное сложение."
    };

    private static final String[] DIVISION_TIPS = {
            "Разделите число на части. Например, 144 / 12 = (120 / 12) + (24 / 12).",
            "Используйте метод подбора. Например, 100 / 25 = 4, так как 25 * 4 = 100.",
            "Тренируйтесь делить числа в уме, начиная с простых примеров.",
            "Попробуйте округлять числа. Например, 98 / 7 ≈ 100 / 7 - 2 / 7.",
            "Используйте визуализацию: представьте, как одно число делится на другое."
    };

    private static final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        problemTextView = findViewById(R.id.problemTextView);
        inputField = findViewById(R.id.inputField);
        sendButton = findViewById(R.id.sendText);
        removeButton = findViewById(R.id.removeButton);
        continueButton = findViewById(R.id.continueButton);
        finishButton = findViewById(R.id.finishButton);
        resultTextView = findViewById(R.id.resultTextView);
        adviceTextView = findViewById(R.id.adviceTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // Устанавливаем обработчики нажатий для кнопок с цифрами
        int[] buttonIds = {
                R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6,
                R.id.button7, R.id.button8, R.id.button9,
                R.id.button0
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> inputField.append(((Button) v).getText().toString()));
        }

        // Обработчик для кнопки "Отправить"
        sendButton.setOnClickListener(v -> checkAnswer());

        // Обработчик для кнопки "Удалить"
        removeButton.setOnClickListener(v -> {
            String text = inputField.getText().toString();
            if (!text.isEmpty()) {
                inputField.setText(text.substring(0, text.length() - 1));
            }
        });

        // Обработчик для кнопки "Продолжить"
        continueButton.setOnClickListener(v -> {
            generateNewProblem();
            continueButton.setVisibility(View.GONE);
            finishButton.setVisibility(View.GONE);
        });

        // Обработчик для кнопки "Завершить"
        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(Plan.this, MainActivity.class);
            startActivity(intent);
        });

        // Запуск таймера при загрузке активности
        startTimer();

        generateNewProblem();
    }

    private void generateNewProblem() {
        String[] problemData = generateProblem(currentDifficulty);
        String problem = problemData[0];
        correctAnswer = Integer.parseInt(problemData[1]);
        currentOperation = problemData[2];

        problemTextView.setText(problem);
        inputField.setText("");
        resultTextView.setText("");
        adviceTextView.setText("");

        // Сброс таймера
        resetTimer();
    }

    private void checkAnswer() {
        String userInput = inputField.getText().toString();
        try {
            int userAnswer = Integer.parseInt(userInput);
            if (userAnswer == correctAnswer) {
                resultTextView.setText("Правильно!");
                currentDifficulty += 1;
            } else {
                resultTextView.setText("Неправильно. Правильный ответ: " + correctAnswer);
                currentDifficulty = Math.max(3, currentDifficulty - 3);
                giveAdvice(currentOperation);
            }
            // Останавливаем таймер
            stopTimer();
            // Показываем кнопки "Продолжить" и "Завершить"
            continueButton.setVisibility(View.VISIBLE);
            finishButton.setVisibility(View.VISIBLE);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Пожалуйста, введите целое число.", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] generateProblem(int x) {
        String operation;
        if (x >= 6) {
            operation = randomOperation(new String[]{"+", "-", "*", "/"});
        } else if (x >= 4) {
            operation = randomOperation(new String[]{"+", "-", "*"});
        } else {
            operation = randomOperation(new String[]{"+", "-"});
        }

        int N;
        switch (operation) {
            case "+":
            case "-":
                N = 1;
                break;
            case "*":
                N = 2;
                break;
            case "/":
                N = 3;
                break;
            default:
                N = 1;
        }

        int num1, num2, correctAnswer;
        if (operation.equals("+") || operation.equals("-") || operation.equals("*")) {
            int secondDigits = random.nextInt((x / N) / 2) + 1;
            int firstDigits = (x / N) - secondDigits;

            num1 = random.nextInt((int) Math.pow(10, firstDigits));
            num2 = random.nextInt((int) Math.pow(10, secondDigits));

            if (operation.equals("-") && num1 < num2) {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }

            correctAnswer = calculateResult(num1, num2, operation);
        } else {
            int divisorDigits = random.nextInt((x / N) / 2) + 1;
            int dividendDigits = (x / N) - divisorDigits;

            int divisor = random.nextInt((int) Math.pow(10, divisorDigits));
            int quotient = random.nextInt((int) Math.pow(10, dividendDigits));
            int dividend = quotient * divisor;

            num1 = dividend;
            num2 = divisor;
            correctAnswer = quotient;
        }

        String problem = num1 + " " + operation + " " + num2;
        return new String[]{problem, String.valueOf(correctAnswer), operation};
    }

    private int calculateResult(int num1, int num2, String operation) {
        switch (operation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                return 0;
        }
    }

    private String randomOperation(String[] operations) {
        return operations[random.nextInt(operations.length)];
    }

    private void giveAdvice(String operation) {
        String[] tips;
        switch (operation) {
            case "+":
                tips = ADDITION_TIPS;
                break;
            case "-":
                tips = SUBTRACTION_TIPS;
                break;
            case "*":
                tips = MULTIPLICATION_TIPS;
                break;
            case "/":
                tips = DIVISION_TIPS;
                break;
            default:
                return;
        }

        String advice = tips[random.nextInt(tips.length)];
        adviceTextView.setText("\nСовет для улучшения:\n" + advice);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                resultTextView.setText("Время вышло! Попробуй считать быстрей.");
                currentDifficulty = Math.max(3, currentDifficulty - 3);
                giveAdvice(currentOperation);
                continueButton.setVisibility(View.VISIBLE);
                finishButton.setVisibility(View.VISIBLE);
            }
        }.start();

        timerRunning = true;
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    private void resetTimer() {
        stopTimer();
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateTimerText();
        startTimer();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }
}
