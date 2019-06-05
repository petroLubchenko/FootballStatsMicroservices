<%@ page import="API.footballstats.client.Models.Footballer" %>
<%@ page import="org.springframework.boot.web.servlet.server.Session" %>
<%@ page import="API.footballstats.client.Models.Team" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 05.06.2019
  Time: 3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Team</title>
</head>
<jsp:include page="menu.jsp" />
<body>

<div class="row justify-content-center">
    <h1>Footballer Info</h1>
</div>
<div class="card mx-auto card-width">
    <form method="post" action="/footballers/update/${footballerobj.id}">
        <div class="card-heading p-2"><h4>Team ID : ${footballerobj.id}</h4>
            <input value="${footballerobj.id}" type="hidden" name="id"/>
        </div>
        <div class="card-heading p-2">
            Firstname: ${footballerobj.firstname} <br>
            <input class="form-control"
                   value="${footballerobj.firstname}"
                   name="firstname"/>
        </div>
        <div class="card-heading p-2">
            Surname: ${footballerobj.surname} <br>
            <input class="form-control"
                   value="${footballerobj.surname}"
                   name="surname"/>
        </div>
        <div class="card-heading p-2">
            Age: ${footballerobj.age} <br>
            <input class="form-control"
                   value="${footballerobj.age}"
                   name="age"/>
        </div>
        <div class="card-heading p-2">
            Games: ${footballerobj.games} <br>
            <input class="form-control"
                   value="${footballerobj.games}"
                   name="games"/>
        </div>
        <div class="card-heading p-2">
            Goals: ${footballerobj.goals} <br>
            <input class="form-control"
                   value="${footballerobj.goals}"
                   name="goals"/>
        </div>
        <div class="card-heading p-2">
            Assists: ${footballerobj.assists} <br>
            <input class="form-control"
                   value="${footballerobj.assists}"
                   name="assists"/>
        </div>
        <div class="card-heading p-2">
            Team: <!--{footballerobj.team} <br>-->
            <select NAME="team">
                <option value="=0, = , =0, = , = }"></option>
                <c:forEach var="item" items="${teams}">
                    <c:if test="${footballerobj.team != null && footballerobj.team.id == item.id}">
                        <option value="${item}" selected>
                    </c:if>
                    <c:if test="${footballerobj.team == null || footballerobj.team.id != item.id}">
                        <option value="${item}">
                    </c:if>
                        <c:out value="${item.id} ${item.name} ${item.city}"></c:out>
                    </option>
                </c:forEach>
            </select>
            <!--<select class="form-control"
                   name="team">
                    <%
                        /*List<Team> ts = (List<Team>) session.getAttribute("teams");
                        Footballer f = (Footballer) session.getAttribute("footballerobj");
                        for(Team t : ts) {
                        if (f != null && f.getTeam() != null && t != null)
                            if (f.getTeam().getId() == t.getId()) {
                    %>
                    <option value="<%//t%>" selected><%//t.getName()%></option>
                    <%
                    } else{
                    %>
                <option value="<%//t%>"><%//t.getName()%></option>
                    <%
                        }}*/
                    %>
            </select>-->
        </div>

        <div class="card-heading p-2 mx-auto" style="max-width: 200px">
            <button type="submit" class="btn btn-success" >Update</button>
            <a class="btn btn-danger" href="/footballers/all">Back</a>
        </div>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
