package com.example.api_auth_sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.api_auth_sample.databinding.ActivityFirstFactorBinding

class FirstFactor : AppCompatActivity() {

    private lateinit var binding: ActivityFirstFactorBinding
    private lateinit var authenticatorsString: String
    private lateinit var fragmentManager: FragmentManager
    private lateinit var authFragment: AuthFragment
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAuthenticatorsString()

        initalizeAuthFragment()

        initializeComponents()
    }

    private fun initializeComponents() {
        binding = ActivityFirstFactorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setAuthenticatorsString() {
        val intent: Intent = intent
        authenticatorsString = intent.getStringExtra("authenticators")!!
    }

    private fun initalizeAuthFragment() {
        fragmentManager = supportFragmentManager
        val mFragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        authFragment = AuthFragment()

        bundle = Bundle()
        bundle.putString("authenticatorsString", authenticatorsString)
        println(authenticatorsString)
        authFragment.arguments = bundle
        mFragmentTransaction.add(R.id.authLayoutView, authFragment).commit()
    }
}
