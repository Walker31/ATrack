package com.example.atrack

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.atrack.data.AttendanceTrack
import com.example.atrack.data.Subject
import com.example.atrack.data.SubjectDao
import com.example.atrack.data.SubjectDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SubjectDaoTest {
    private lateinit var itemDao: SubjectDao
    private lateinit var inventoryDatabase: SubjectDatabase
    private val item1 = Subject(1, "Apples", "10.0", 20,50,0f)
    private val item2 = Subject(2, "Bananas", "15.0", 97,150,0f)
    private val item3=AttendanceTrack(2,"Physics","PHIR","14/7/2022",true)
    private val item4=AttendanceTrack(3,"Physics","PHIR","14/7/2022",true)
    private val item5=AttendanceTrack(4,"Physics","PHIR","14/7/2022",true)


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, SubjectDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        itemDao = inventoryDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetCount()= runBlocking {
        addTwoItemsToDb()
        val count =itemDao.getDateCount(subName= "PHIR")
        assertEquals(3,count)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = itemDao.getItem(1)
        assertEquals(item.first(), item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        itemDao.deleteItem(item1)
        itemDao.deleteItem(item2)
        val allItems = itemDao.getAllItems().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemsToDb()
        itemDao.update(Subject(1, "Apples", "15.0", 25,50,0f))
        itemDao.update(Subject(2, "Bananas", "5.0", 50,100,0f))

        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], Subject(1, "Apples", "15.0", 25,50,0f))
        assertEquals(allItems[1], Subject(2, "Bananas", "5.0", 50,100,0f))
    }

    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    private suspend fun addTwoItemsToDb() {
        itemDao.insertDate(item3)
        itemDao.insertDate(item4)
        itemDao.insertDate(item5)
    }
}