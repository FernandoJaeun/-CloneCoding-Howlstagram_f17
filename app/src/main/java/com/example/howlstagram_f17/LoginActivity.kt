package com.example.howlstagram_f17

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        email_login_button.setOnClickListener{ signinAndSignup()}

    }

    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // 유저 정보 생성
                moveMainPage(task.result?.user) // safe call
            } else if (task.exception?.message.isNullOrEmpty()) {
                // 에러 메세지 띄우기
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            } else {
                // 가입했다면 로그인 해라
                signinEmail()
            }
        }
    }

    fun signinEmail() {
        auth?.createUserWithEmailAndPassword(
            email_edittext.text.toString(),
            password_edittext.text.toString()
        )?.addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // 로그인
            } else {
                // 에러 메세지 표시
            }
        }
    }

    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
