package com.mathkids.sligamer.mathkids

import android.app.Fragment
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
        viewReturn = inflater.inflate(R.layout.selectlevel_fragment, container, false)

        lvl1IMG = viewReturn.findViewById(R.id.imageButton)
        lvl1IMG.setOnClickListener{v -> startPlay(GamePlayActivity())}
        lvl2IMG = viewReturn.findViewById(R.id.imageButton2)
        lvl2IMG.setOnClickListener{v -> startPlay(GamePlayActivity())}
        lvl3IMG = viewReturn.findViewById(R.id.imageButton3)
        lvl3IMG.setOnClickListener{v -> startPlay(GamePlayActivity())}

        return viewReturn
    }

    private fun startPlay(v: GamePlayActivity){
        this.startPlay(GamePlayActivity())
    }
}