<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 05.06.2019
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Team ${team.name}</title>
</head>
<jsp:include page="menu.jsp" />
<body>

<div class="row justify-content-center">
    <h1>Team ${team.name} Info</h1>
</div>
<div class="card mx-auto card-width">
    <form method="post" action="/teams/update/${team.id}">
        <div class="card-heading p-2"><h4>Team ID : ${team.id}</h4>
            <input value="${team.id}" type="hidden"/>
        </div>
        <div class="card-heading p-2">
            Name: ${team.name} <br>
            <input class="form-control"
                   value="${team.name}"
                   name="name"/>
        </div>
        <div class="card-heading p-2">
            Stadium Name: ${team.stadiumname} <br>
            <input class="form-control"
                   name="stadiumname"
                   value="${team.stadiumname}"/>
        </div>
        <div class="card-heading p-2">
            City: ${team.city}
            <br>
            <input class="form-control"
                   name="city"
                   value="${team.city}"/>
        </div>
        <div class="card-heading p-2">
            Count of seasons: ${team.seasonscount}
            <br>
            <input class="form-control"
                   name="seasonscount"
                   value="${team.seasonscount}"/>
        </div>
        <div class="card-heading p-2">
            Count of points: ${team.points}
            <br>
            <input class="form-control"
                   name="points"
                   value="${team.points}"/>
        </div>
        <div class="card-heading p-2">
            Championship:
            <select NAME="championship">
                <option value="=0, = , = }"></option>
                <c:forEach var="item" items="${championships}">
                    <c:if test="${team.championship != null && team.championship.id == item.id}">
                        <option value="${item}" selected>
                    </c:if>
                    <c:if test="${team.championship == null || team.championship.id != item.id}">
                        <option value="${item}">
                    </c:if>
                    <c:out value="${item.id} ${item.name} ${item.abbreviatedname}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="card-heading p-2 mx-auto" style="max-width: 200px">
            <button type="submit" class="btn btn-success" >Update</button>
            <button type="reset" class="btn btn-danger" href="/teams/all">Back</button>
        </div>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
