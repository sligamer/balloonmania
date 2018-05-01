package com.mathkids.sligamer.mathkids

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class SelectLevelFragment : Fragment(){
    // Please Check; Not sure if this is correct?? (Abbey)

    private lateinit var lvl1IMG : ImageButton
    private lateinit var lvl2IMG : ImageButton
    private lateinit var lvl3IMG : ImageButton

    private lateinit var viewReturn: View

    companion object {
        fun newInstance(): SelectLevelFragment {
            return SelectLevelFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewReturn = inflater.inflate(R.layout.selectlevel_layout, container, false)

        lvl1IMG = viewReturn.findViewById(R.id.btnLvL1)
        lvl1IMG.setOnClickListener{v -> startPlay(0)}
        lvl2IMG = viewReturn.findViewById(R.id.btnLvL2)
        lvl2IMG.setOnClickListener{v -> startPlay(1)}
        lvl3IMG = viewReturn.findViewById(R.id.btnLvL3)
        lvl3IMG.setOnClickListener{v -> startPlay(2)}

        return viewReturn
    }

    private fun startPlay(chosenLevel: Int) {
        val playIntent = Intent(this.context, GamePlayActivity().javaClass)
        playIntent.putExtra("level", chosenLevel)
        this.startActivity(playIntent)
    }
}