package org.example.test;


import org.example.Util;
import org.junit.*;

public class UtilTest {

    @Test
    public void testisIdPositive(){
        Assert.assertTrue(Util.isIdPositive(0));
        Assert.assertTrue(Util.isIdPositive(1000));
        Assert.assertTrue(Util.isIdPositive(110));
    }
    @Test
    public void testCheckName(){
        Assert.assertTrue(Util.checkName("Ваня"));
        Assert.assertTrue(Util.checkName("Ваня"));
        Assert.assertTrue(Util.checkName("даня гловацкий"));
        Assert.assertTrue(Util.checkName("гловацкий"));

    }
    @Test
    public void testChechAge(){
        Assert.assertTrue(Util.checkAge(0));
        Assert.assertTrue(Util.checkAge(1000));
        Assert.assertTrue(Util.checkAge(10));
    }
    @Test
    public void testCheckRoom(){
        Assert.assertTrue(Util.checkRoom(0));
        Assert.assertTrue(Util.checkRoom(88));
        Assert.assertTrue(Util.checkRoom(17));
    }
    @Test
    public void testCheckType(){
        Assert.assertTrue(Util.checkType("люкс"));
        Assert.assertTrue(Util.checkType("обычный"));
        Assert.assertTrue(Util.checkType("люкс"));
    }



    @BeforeClass
    public static void allTestStarted(){
        System.out.println("Начало тестирования");
    }

    @AfterClass
    public static void allTestFinished(){
        System.out.println("Конец тестирования");
    }
    @Before
    public void testStarted() {
        System.out.println("Запуск теста");
    }
    @After
    public void testFinished() {
        System.out.println("Завершение теста");
    }

}
