package ru.ultrabasic.bstutp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cookie")
public class MakeCookie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Кукан
        Cookie ck = new Cookie("CoolCookie", "CoolCookieValue2");
        // Почему-то без этого параметра куканы не устанавливаются
        ck.setSecure(true);
        // Время истечения в секундах, 0 если мы хотим чтобы кукан существовал только текущую сессию, иначе - время в секундах
        ck.setMaxAge(6 * 365 * 24 * 60 * 60);
        // Пишем куканы в заголовок
        resp.addCookie(ck);
    }
}
