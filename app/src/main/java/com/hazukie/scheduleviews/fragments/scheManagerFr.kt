package com.hazukie.scheduleviews.fragments

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.hazukie.cskheui.Crialoghue.Crialoghue
import com.hazukie.cskheui.Crialoghue.Crialoghue.CustomBuilder
import com.hazukie.cskheui.Crialoghue.Crialoghue.TxtBuilder
import com.hazukie.cskheui.LeditView.LeditView
import com.hazukie.cskheui.LetxtView.LetxtView
import com.hazukie.scheduleviews.R
import com.hazukie.scheduleviews.activity.ScheEditActivity
import com.hazukie.scheduleviews.base.FragmentContainerAct
import com.hazukie.scheduleviews.binders.HorionCardBinder
import com.hazukie.scheduleviews.binders.UniBinder
import com.hazukie.scheduleviews.custom.CRecyclerView
import com.hazukie.scheduleviews.fileutil.BasicOpts
import com.hazukie.scheduleviews.fileutil.FileRootTypes
import com.hazukie.scheduleviews.fileutil.OftenOpts
import com.hazukie.scheduleviews.models.*
import com.hazukie.scheduleviews.statics.Statics
import com.hazukie.scheduleviews.utils.DisplayHelper
import com.hazukie.scheduleviews.utils.PopupUtil


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [scheManagerFr.newInstance] factory method to
 * create an instance of this fragment.
 */
class scheManagerFr : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recy:RecyclerView
    private lateinit var emptyLay:LinearLayout
    private lateinit var mobs: MutableList<Any>
    private lateinit var mdp:MultiTypeAdapter

    private lateinit var oftenOpts:OftenOpts
    private lateinit var basicOpts:BasicOpts

    private lateinit var scts: MutableList<ScheWithTimeModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.fragment_sche_manage, container, false)

        //addView
        view.findViewById<TextView>(R.id.frag_sche_manage_add).apply {
            setOnClickListener{
                FragmentContainerAct.startActivityWithLoadUrl(activity, ScheCreateBeforeFrag::class.java)
            }
        }

        recy=view.findViewById(R.id.frag_sche_manage_recy)
        emptyLay=view.findViewById(R.id.frag_sche_empty)

        oftenOpts = OftenOpts.getInstance(context)
        basicOpts = BasicOpts.getInstance(context)


        //获取记录在案的所有文件数据
        scts = oftenOpts.recordedScts


        val horionCardBinder = HorionCardBinder()
        //设置文件详情展开功能
        horionCardBinder.setExpandListener { v12: View?, conView: TextView, descript: String? ->
            if (conView.visibility == View.GONE) conView.visibility = View.VISIBLE
            else conView.visibility = View.GONE
        }


        //设置删除文件功能
        horionCardBinder.setDelListener { v13: View?, hrc: HoricardModel ->
            val indx_ = mobs.indexOf(hrc)
            val cih = TxtBuilder()
                .addTitle("课表文件")
                .addContent("确定删除此课表文件吗?")
                .onConfirm { cDialoh: Crialoghue, view: View? ->
                    for (sct in scts) {
                        if (sct.getScheName() == hrc.title) {
                            mobs.remove(hrc)
                            mdp.notifyItemRemoved(indx_)
                            executeDel(sct);
                            break
                        }
                    }
                    DisplayHelper.Infost(activity, "删除成功！")
                    controlEmpty();
                    cDialoh.dismiss()
                }
                .build(activity)
            cih.show()
        }


        //重命名、更改关联作息表
        horionCardBinder.setSettingListener { _: View?, hrc: HoricardModel? ->
            if (hrc != null) {
                showSettinialoh(hrc)
            }
        }

        //打开课表文件编辑及保存
        horionCardBinder.setOnOpenDoc { sche: String?, time: String? ->
            ScheEditActivity.startActivityWithData(activity, sche, time)
        }

        mobs = mutableListOf()
        mdp = MultiTypeAdapter()
        mdp.register(HoricardModel::class.java, horionCardBinder)

        recy.adapter = mdp
        mdp.items = mobs

        controlEmpty()
        return view
    }

    //打开编辑对话框
    private fun showSettinialoh(horic: HoricardModel) {
        val lps = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val firedit = LeditView.Builder().addLeftile("课表名称").addContent(horic.title).create(activity)

        val secedit: LinearLayout = LetxtView.Builder()
            .addLeftile("关联作息")
            .addRightile(horic.subtitle)
            .addInterceptor { _: TextView?, textView1: TextView, _: LinearLayout? ->
                textView1.setOnClickListener {
                    onEditTime(horic, textView1)
                }
            }
            .create(activity)

        val doubledit = LinearLayout(activity)
        doubledit.orientation = LinearLayout.VERTICAL
        doubledit.layoutParams = lps
        doubledit.addView(firedit)
        doubledit.addView(secedit)
        val croh = CustomBuilder()
            .addTitle("编辑课表")
            .addView(doubledit)
            .onConfirm { crialoghue: Crialoghue, viewGroup: ViewGroup ->
                try {
                    var isDuplicate = false
                    val root = viewGroup.getChildAt(0) as LinearLayout
                    val fireditLay = root.getChildAt(0) as LinearLayout
                    val editV = fireditLay.getChildAt(1) as EditText
                    val mSchName = editV.text.toString().replace("\\s*".toRegex(), "")
                    if (mSchName == horic.getTitle()) {
                        mdp.notifyItemChanged(mobs.indexOf(horic), "updating")
                        crialoghue.dismiss()
                    } else {
                        if (mSchName.isNotEmpty()) {
                            for (sct in scts) {
                                if (sct.getScheName() == mSchName) {
                                    isDuplicate = true
                                    break
                                }
                            }
                            if (!isDuplicate) {
                                val isRename: Boolean = basicOpts.rename(FileRootTypes.sches, horic.title + ".txt", "$mSchName.txt")
                                Log.i("showSettinialoh>>", "isRename=$isRename")
                                if (isRename) {
                                    horic.title = mSchName
                                    mdp.notifyItemChanged(mobs.indexOf(horic), "updating")
                                    refreshScts()
                                    crialoghue.dismiss()
                                } else {
                                    DisplayHelper.Infost(context, "修改文件名失败！")
                                }
                            } else {
                                DisplayHelper.Infost(activity, "文件名已重复，请重新输入！")
                            }
                        } else {
                            DisplayHelper.Infost(activity, "字数不能为0，请重新输入！ ")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .build(activity)
        croh.show()
    }


    /*
    对话框中作息表的逻辑处理
    可以更换作息表
     */
    private fun onEditTime(horic: HoricardModel, txt: TextView) {
        val pou = PopupUtil(context)
        val pouwin = pou.initDefaultPopup(120)
        pou.initDefaultViews { textView: TextView, recy: CRecyclerView, multiAdp: MultiTypeAdapter, viewlist: MutableList<Any?> ->
            val mSelectedTime = txt.text.toString()
            recy.setMaxHeight(200)
            textView.text = "默认作息表"
            textView.setOnClickListener { v: View? ->
                txt.text = "默认作息表"
                horic.subtitle = "默认作息表"
                horic.description = getTimedescription("默认作息表")
                if (mSelectedTime != "默认作息表") refreshScts()
                pouwin.dismiss()
            }
            val uni = UniBinder()
            uni.setClickListener { v: View?, uni1: Unimodel ->
                horic.subtitle = uni1.title
                horic.description = getTimedescription(uni1.title)
                txt.text = uni1.title
                if (mSelectedTime != uni1.title) refreshScts()
                pouwin.dismiss()
            }
            multiAdp.register(Unimodel::class.java, uni)
            val tims: List<TimeModel> = oftenOpts.recordTms
            for (i in tims.indices) {
                val ti = tims[i]
                viewlist.add(Unimodel(ti.id, ti.getTimeName()))
            }
        }
        pouwin.showAsDropDown(txt, 0, 0, Gravity.END)
    }


    private fun getTimedescription(name: String): String? {
        val sdes: String
        val thdm: TimeHeadModel = if(name.isEmpty()) oftenOpts.getThm(Statics.default_time_file_txt) else oftenOpts.getThm("$name.txt")
        sdes = thdm.outputBasics()
        return sdes
    }

    //处理删除列表中的数据
    //写入修改后数据
    private fun executeDel(del: ScheWithTimeModel) {
        val isDel: Boolean = basicOpts.delete(FileRootTypes.sches, del.scheName)
        Log.i("ExcecuteDel>>>", "delete_item= " + del.scheName + " status=" + isDel)
        refreshScts()
    }

    //刷新数据，并将数据写入文件
    private fun refreshScts() {
        val neo_scts: MutableList<ScheWithTimeModel> = ArrayList()
        for (obj in mobs) {
            val hor = obj as HoricardModel
            neo_scts.add(ScheWithTimeModel(mobs.indexOf(obj), hor.title + ".txt", hor.subtitle + ".txt")
            )
            Log.i("ScheManageFrag>>", "time=" + hor.subtitle)
        }
        oftenOpts.putRawSctList(neo_scts)
    }

    private fun controlEmpty() {
        if (mobs.size > 0) {
            recy.visibility = View.VISIBLE
            emptyLay.visibility = View.GONE
        } else {
            recy.visibility = View.GONE
            emptyLay.visibility = View.VISIBLE
        }
    }

    //数据更新
    fun reshData() {
        if(this::mobs.isInitialized){
            mobs.clear()
            scts.clear()
            scts.addAll(oftenOpts.recordedScts)
            if (scts.size > 0) {
                for (i in scts.indices) {
                    val sct = scts[i]
                    val sdes = getTimedescription(sct.getTimeName())
                    mobs.add(HoricardModel(i, sct.getScheName(), sct.getTimeName(), sdes))
                }
            }
            mdp.notifyItemRangeChanged(0,mobs.size)
            controlEmpty()
            Log.i("onResume>>>", "sct_datas has updating!")
        }
    }

    override fun onResume() {
        super.onResume()
        reshData()
    }
}