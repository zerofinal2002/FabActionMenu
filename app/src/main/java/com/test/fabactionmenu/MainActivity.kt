package com.test.fabactionmenu

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var isShowSheet: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        showFab(true, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowSheet) {
                hideSheet()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun hideSheet() {
        val cx = sheet.width / 2
        val cy = sheet.height

        // get the final radius for the clipping circle
        val initialRadius = sheet.width

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(sheet, cx, cy, initialRadius.toFloat(), 0f)
        anim.duration = 200
        // make the view visible and start the animation
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                sheetBackground.visibility = View.GONE
            }

            override fun onAnimationEnd(animation: Animator) {
                sheet.visibility = View.INVISIBLE
                val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(baseContext, R.anim.fab_back_sheet)
                fab.visibility = View.VISIBLE
                hyperspaceJumpAnimation.start()
                isShowSheet = false
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        anim.start()
    }

    private fun showFab(isShow: Boolean) {
        if (isShow) {
            appbar.setExpanded(true)
            fab.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.scale_show))
        } else {
            fab.startAnimation(AnimationUtils.loadAnimation(baseContext, R.anim.scale_hide))
        }
        fab.visibility = (if (isShow) View.VISIBLE else View.GONE)
    }

    private fun showFab(isShow: Boolean, type: Int?) {
        if (isShow) {
            fab.setOnClickListener({

                val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(baseContext, R.anim.fab_to_sheet)
                hyperspaceJumpAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {

                    }

                    override fun onAnimationEnd(animation: Animation) {
                        fab.visibility = View.INVISIBLE
                        // previously invisible view
                        //View myView = findViewById(R.id.my_view);

                        // get the center for the clipping circle
                        val cx = sheet.width / 2
                        val cy = sheet.height

                        // get the final radius for the clipping circle
                        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

                        // create the animator for this view (the start radius is zero)
                        val anim = ViewAnimationUtils.createCircularReveal(sheet, cx, cy, 0f, finalRadius)
                        anim.duration = 200
                        // make the view visible and start the animation
                        sheet.visibility = View.VISIBLE
                        anim.start()
                    }

                    override fun onAnimationRepeat(animation: Animation) {

                    }
                })
                fab.startAnimation(hyperspaceJumpAnimation)
                sheetBackground.visibility = View.VISIBLE
                sheetBackground.setOnClickListener({ hideSheet() })
                isShowSheet = true
            })
        }
        showFab(isShow)

    }
}
