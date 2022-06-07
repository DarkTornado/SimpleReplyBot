package com.darktornado.simplereplybot

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.darktornado.simplereplier.ReplyData
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private val names = ArrayList<String>()
    private val data = ArrayList<ReplyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = 1
        val on = Switch(this)
        on.text = "봇 활성화"
        on.isChecked = Bot.readBoolean(this, "bot_on")
        on.setOnCheckedChangeListener { swit, onoff ->
            Bot.saveBoolean(this, "botOn", onoff)
        }
        layout.addView(on)

        updateData(null)
        val adapter: BaseAdapter = object : BaseAdapter() {

            override fun getCount(): Int {
                return names.size
            }

            override fun getItem(position: Int): Any {
                return names[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
                view.text = names[position]
                return view
            }
        }

        val add = Button(this)
        add.text = "추가"
        add.setOnClickListener { v: View? -> editDialog(ReplyData(), -1, adapter) }
        layout.addView(add)
        val list = ListView(this)
        list.adapter = adapter
        list.isFastScrollEnabled = true
        list.onItemClickListener = OnItemClickListener {
            adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long ->
            editDialog(data.get(pos), pos, adapter)
        }
        layout.addView(list)

        val params = LinearLayout.LayoutParams(-1, -1)
        val mar = dip2px(16)
        params.setMargins(mar, mar, mar, mar)
        layout.layoutParams = params
        setContentView(layout)
    }

    fun editDialog(data: ReplyData, pos: Int, adapter: BaseAdapter) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("반응할 말 추가")
        val layout = LinearLayout(this)
        layout.orientation = 1

        val txt1 = TextView(this)
        txt1.text = "이 방에서..."
        txt1.textSize = 17f
        layout.addView(txt1)
        val txt2 = EditText(this)
        txt2.hint = "비워두면 모든 방에서 작동"
        txt2.setText(data.room)
        layout.addView(txt2)

        val radios = RadioGroup(this)
        val types = Array(3) { "" }
        types[0] = "모든 채팅방"
        types[1] = "1:1 채팅방"
        types[2] = "단체 채팅방"
        for (n in types.indices) {
            val radio = RadioButton(this)
            radio.text = types[n]
            radio.id = n
            radios.addView(radio)
        }
        radios.check(data.type)

        val txt3 = TextView(this)
        txt3.text = "\n이 말을 하면..."
        txt3.textSize = 17f
        layout.addView(txt3)
        val txt4 = EditText(this)
        txt4.hint = "내용을 입력하세요"
        txt4.setText(data.input)
        layout.addView(txt4)

        val txt5 = TextView(this)
        txt5.text = "\n이렇게 답변..."
        txt5.textSize = 17f
        layout.addView(txt5)
        val txt6 = EditText(this)
        txt6.hint = "내용을 입력하세요"
        txt6.setText(data.output)
        layout.addView(txt6)

        val pad = dip2px(16)
        layout.setPadding(pad, pad, pad, pad)
        val scroll = ScrollView(this)
        scroll.addView(layout)
        dialog.setView(scroll)
        dialog.setNegativeButton("취소", null)
        if (pos != -1) dialog.setNeutralButton("삭제") { _dialog, which ->
            try {
                this.data.removeAt(pos)
                updateData(saveData())
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                toast(e.toString())
            }
        }
        dialog.setPositiveButton("확인") { _dialog, which ->
            try {
                val room = txt2.text.toString()
                val input = txt4.text.toString()
                val output = txt6.text.toString()
                val type = radios.checkedRadioButtonId
                this.data.add(ReplyData(input, output, room, type))
                updateData(saveData())
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                toast(e.toString())
            }
        }
        dialog.show()
    }

    fun updateData(str: String?) {
        try {
            val cache: String?
            if (str != null) cache = str
            else cache = Bot.readString(this, "reply_data")
            names.clear()
            data.clear()
            if (cache != null) {
                val data = JSONArray(cache)
                for (n in 0 until data.length()) {
                    this.data.add(ReplyData(data.getJSONObject(n)))
                    names.add(this.data[n].input + " → " + this.data[n].output)
                }
            }
        } catch (e: Exception) {
            toast("반응할 말 목록 불러오기 실패.\n$e")
        }
    }

    fun saveData() : String {
        if (data.size == 0) {
            Bot.saveString(this, "reply_data", "[]")
            return "[]"
        }
        val str = StringBuilder("[")
        str.append(data[0].toJSON())
        for (n in 1 until data.size) {
            str.append(",").append(data[n].toJSON())
        }
        str.append("]")
        Bot.saveString(this, "reply_data", str.toString())
        return str.toString()
    }


    fun dip2px(dips: Int): Int {
        return Math.ceil((dips * this.resources.displayMetrics.density).toDouble()).toInt()
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}