package com.example.wordle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import FourLetterWordList
import android.util.Log

class MainActivity : AppCompatActivity() {
    private var wordToGuess = FourLetterWordList.getRandomFourLetterWord()
    private var gameWon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    //Read in et_input_guess
        val editText = findViewById<EditText>(R.id.et_input_guess)
        var inputTextStr =""
        var guessCount = 0

        val guess2view = findViewById<TextView>(R.id.GuessNum2_label)
        val guess3view = findViewById<TextView>(R.id.GuessNum3_label)
        val guess1viewlabel = findViewById<TextView>(R.id.GuessNum1Check_label)
        val guess2viewlabel = findViewById<TextView>(R.id.GuessNum2Check_label)
        val guess3viewlabel = findViewById<TextView>(R.id.GuessNum3Check_label)

        val guess1view = findViewById<TextView>(R.id.GuessNum1_input)
        val guess1rviewlabel = findViewById<TextView>(R.id.GuessNum1Check_input)
        val guess2rviewlabel = findViewById<TextView>(R.id.GuessNum2_input)
        val guess3rviewlabel = findViewById<TextView>(R.id.GuessNum3Check_input)
        val guess4rviewlabel = findViewById<TextView>(R.id.GuessNum3_input)
        val guess5rviewlabel = findViewById<TextView>(R.id.GuessNum4Check_input)

        val newGameBTN =findViewById<Button>(R.id.new_game_btn)
        val correctWord = findViewById<TextView>(R.id.correct_word)
        correctWord.text = wordToGuess
        Log.i("Word", "This WORD IS $wordToGuess")
        newGameBTN.setOnClickListener {
            //choose new word
            gameWon = false
            wordToGuess = FourLetterWordList.getRandomFourLetterWord()
            correctWord.text = wordToGuess
            Log.i("Word", "This WORD IS $wordToGuess")




            correctWord.visibility = View.INVISIBLE
            //set everything invisible

            guess1view.visibility = View.INVISIBLE
            guess1viewlabel.visibility =  View.INVISIBLE
            guess1rviewlabel.visibility = View.INVISIBLE

            guess2view.visibility = View.INVISIBLE
            guess2rviewlabel.visibility = View.INVISIBLE

            guess2viewlabel.visibility = View.INVISIBLE
            guess3rviewlabel.visibility = View.INVISIBLE

            guess3view.visibility = View.INVISIBLE
            guess4rviewlabel.visibility = View.INVISIBLE

            guess3viewlabel.visibility = View.INVISIBLE
            guess5rviewlabel.visibility = View.INVISIBLE
            //set guess count to 0
            guessCount = 0

            //set button back invisible
            newGameBTN.visibility = View.INVISIBLE
            editText.visibility =View.VISIBLE


        }

        editText.setOnEditorActionListener { _, keyCode, event ->
            if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                || keyCode == EditorInfo.IME_ACTION_DONE) {
                val userGuessInput = editText.text.toString()
                inputTextStr = userGuessInput
                // Hide the soft keyboard
                hideSoftKeyboard(editText)
                //Toast.makeText(applicationContext, inputTextStr, Toast.LENGTH_SHORT).show()

                if(inputTextStr.length < 4){
                    Toast.makeText(applicationContext, "Minimum Length is 4", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true //exit the function early
                }

                if(!inputTextStr.matches(Regex("[a-zA-Z]+"))){
                    Toast.makeText(applicationContext, "Only Letters from A-Z", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true //exit the function early


                }

                guessCount++
                editText.text.clear()


                if(guessCount == 1 ){ //first guess
                    guess1view.text = inputTextStr
                    guess1view.visibility = View.VISIBLE

                    guess1viewlabel.visibility =  View.VISIBLE
                    guess1rviewlabel.visibility = View.VISIBLE
                    guess1rviewlabel.text = checkGuess(inputTextStr)
                }

                if(guessCount == 2){ //second guess
                    guess2view.visibility = View.VISIBLE
                    guess2rviewlabel.text = inputTextStr
                    guess2rviewlabel.visibility = View.VISIBLE

                    guess2viewlabel.visibility = View.VISIBLE
                    guess3rviewlabel.text = checkGuess(inputTextStr)
                    guess3rviewlabel.visibility = View.VISIBLE

                }

                if (guessCount == 3){ //third guess

                    guess3view.visibility = View.VISIBLE
                    guess4rviewlabel.text = inputTextStr
                    guess4rviewlabel.visibility = View.VISIBLE

                    guess3viewlabel.visibility = View.VISIBLE
                    guess5rviewlabel.text = checkGuess(inputTextStr)
                    guess5rviewlabel.visibility = View.VISIBLE
                    editText.visibility =View.INVISIBLE
                }

                if(gameWon){
                    Toast.makeText(applicationContext, "YOU WIN!", Toast.LENGTH_LONG).show()
                }

                if(gameWon || guessCount==3){
                    //show new game button and have button revert code back to start with new word
                    editText.visibility =View.INVISIBLE
                    correctWord.visibility = View.VISIBLE
                    newGameBTN.visibility = View.VISIBLE
                }

                if(!gameWon && guessCount==3){
                    Toast.makeText(applicationContext, "YOU LOSE! TRY AGAIN", Toast.LENGTH_SHORT).show()
                }




                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }
    // Helper function to hide the soft keyboard
    private fun hideSoftKeyboard(editText: EditText) {
        val inputMethodManager =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        checkWin(result)
        return result
    }

    private fun checkWin(result: String)  {
        if (result=="OOOO"){
            gameWon = true
        }
        return
    }

}