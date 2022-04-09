package com.bignerdranch.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var sceneView : View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private lateinit var sunReflection : View
    private lateinit var seaView : View

    private var sunState : Boolean = true

    // TODO - TRY AND IMPROVE THE ICON LOOK OF THIS APP.

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }

    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()  // hide actionBar

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.imgSun)
        skyView = findViewById(R.id.imgSky)
        sunReflection = findViewById(R.id.imgSunReflection)
        seaView = findViewById(R.id.imgSeaView)

        sceneView.setOnClickListener {
            animation()
        }
    }

    private fun animation() {
        sunState = if (sunState) {
            startAnimation()
            false // will reverse the value

        } else {
            reverseAnimation()
            true // and so on

        }
    }

    // function will move the top of the sunView to the bottom of the skyView
    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()


        // Object animator is a property animator that repeatedly calls different setter functions with different values to move the
        // views around the screen
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)

        // will change the speed of our animation's movement
        heightAnimator.interpolator = AccelerateInterpolator()

        // This will animate the sky from blue_color to sunset_color
        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())  // ArgbEvaluator() tells ObjectAnimator exactly what the value is.

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())



        // BASED ON A CHALLENGE.
        val sunYHeatStart = 0f
        val sunYHeatEnd = 360f

        val heatAnimator = ObjectAnimator
            .ofFloat(sunView, "rotation", sunYHeatStart, sunYHeatEnd)
            .setDuration(9000)

        heatAnimator.repeatCount = ObjectAnimator.INFINITE


        // BASED ON A CHALLENGE. Sun reflection
        val refStart = sunReflection.top.toFloat()
        val refEnd = seaView.height.toFloat()

        val sunReflectAnimator = ObjectAnimator
            .ofFloat(sunReflection, "y", refStart, refEnd)
            .setDuration(3000)


        /** This is a simpler Implementation of animator.start() in which we will start all the animation at the same time. **/

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)  // play heightAnimator with sunsetSky, also play heightAnimator before nightSky
            .with(sunsetSkyAnimator)
            .with(sunReflectAnimator)
            .with(heatAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()
    }

    // Our reverse animation.
    private fun reverseAnimation() {

        /** Second animator set **/

        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()

        val heightAnimator2 = ObjectAnimator
            .ofFloat(sunView, "y", sunYEnd, sunYStart)
            .setDuration(3000)
        heightAnimator2.interpolator = AccelerateInterpolator()


        val sunsetSkyAnimator2 = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator2.setEvaluator(ArgbEvaluator())


        val blueSkyColor = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(1500)
        blueSkyColor.setEvaluator(ArgbEvaluator())


        val refStart = sunReflection.top.toFloat()
        val refEnd = seaView.height.toFloat()

        val sunReflectAnimator = ObjectAnimator
            .ofFloat(sunReflection, "y", refEnd, refStart)
            .setDuration(3000)

        val animatorSet2 = AnimatorSet()
        animatorSet2.play(heightAnimator2)  // play heightAnimator with sunsetSky, also play heightAnimator before blueSky
            .with(sunsetSkyAnimator2)
            .with(sunReflectAnimator)
            .before(blueSkyColor)

        animatorSet2.start()
    }

}