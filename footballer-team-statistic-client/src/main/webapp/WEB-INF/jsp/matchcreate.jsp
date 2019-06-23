<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 09.06.2019
  Time: 21:55
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
    <title>Footballer ${footballerobj.firstname} ${footballerobj.surname}</title>
</head>
<jsp:include page="menu.jsp" />
<body>

<div class="row justify-content-center">
    <h1>Footballer Info</h1>
</div>
<div class="card mx-auto card-width">
    <form method="post" action="/championships/match">
        <div class="card-heading p-2"><h4>Match ID : ${match.id}</h4>
            <input value="${match.id}" type="hidden" name="id"/>
        </div>
        <div class="card-heading p-2"><h4>Home team id : ${match.homeTeam}</h4>
            <input value="${match.homeTeam}" type="hidden" name="homeTeam"/>
        </div>
        <div class="card-heading p-2">
            Team: <!--{footballerobj.team} <br>-->
            <select NAME="awayTeam">
                <option value="=0, = , =0, = , = }"></option>
                <c:forEach var="item" items="${teams}">
                    <option value="${item.id}">
                        <c:out value="${item.name} ${item.city}"></c:out>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="card-heading p-2 mx-auto" style="max-width: 200px">
            <button type="submit" class="btn btn-success" >Create</button>
            <a class="btn btn-danger" href="/championships">Back</a>
        </div>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
