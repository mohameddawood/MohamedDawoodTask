package com.task.football

import com.task.football.utils.formatDate
import com.task.football.utils.formatDateToCompare
import com.task.football.utils.getImageUrl
import org.junit.Assert
import org.junit.Test

class UtilsTest {


    @Test
    fun testDateFormat(){
        val tDate = "2022-02-10T19:48:37Z"
        Assert.assertEquals("07:02",tDate.formatDate())
    }

    @Test
    fun testDateFormatToCompare(){
        val tDate = "2022-02-10T19:48:37Z"
        Assert.assertEquals("2022/02/10",tDate.formatDateToCompare())
    }
}