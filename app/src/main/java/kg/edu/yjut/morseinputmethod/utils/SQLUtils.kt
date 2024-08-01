package kg.edu.yjut.morseinputmethod.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.LinearLayout
import kg.edu.yjut.morseinputmethod.entity.MorseCode
import kg.edu.yjut.morseinputmethod.helper.AssetsDatabaseManager


class SQLUtils {
    private var context: Context? = null
    private lateinit var db: SQLiteDatabase
    constructor(context: Context) {
        this.context = context
        AssetsDatabaseManager.initManager(context)
        val mg = AssetsDatabaseManager.getManager()
            // 通过管理对象获取数据库
        db =  mg.getDatabase("mdm.db")
    }
    @SuppressLint("Range")
    fun getChineseMorseByStr(code: String, page:Int): List<MorseCode> {
        val pageSize = 30
        // 如果 code 是数字开头的 且 为纯数字
        if (code.matches("^[0-9]+$".toRegex())) {
            var sql = "select * from mdm where key like '$code%' limit $pageSize offset ${(page-1)*pageSize}"
            var cursor = db.rawQuery(sql, null)
            var list = mutableListOf<MorseCode>()

            // 对数据库进行查询 , 查询以 code 开头的 12 条数据
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var chinese = cursor.getString(cursor.getColumnIndex("value"))
                    var code = cursor.getString(cursor.getColumnIndex("key"))
                    list.add(MorseCode(code,chinese))
                }
            }
            return list
        }else{
            var sql = "select * from mdm where value like '$code%' limit $pageSize offset ${(page-1)*pageSize}"
            var cursor = db.rawQuery(sql, null)
            var list = mutableListOf<MorseCode>()
            // 对数据库进行查询 , 查询以 code 开头的 12 条数据
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var chinese = cursor.getString(cursor.getColumnIndex("value"))
                    var code = cursor.getString(cursor.getColumnIndex("key"))
                    list.add(MorseCode(code, chinese))
                }
            }
            return list
        }

    }


}