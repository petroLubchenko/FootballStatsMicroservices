<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 07.06.2019
  Time: 2:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Teams of ${championship.abbreviatedname}</title>
</head>
<jsp:include page="menu.jsp" />
<body>

<div class="col-10 offset-1">

    <div class="center-block headers-region">
        <h1 class="header-panel">Championship ${championship.abbreviatedname} (${championship.name})</h1>
    </div>

    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Name</th>
            <th scope="col">City</th>
            <th scope="col">Stadium</th>
            <th scope="col">Points</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach  items="${teams}" var="e">
            <tr>
                <th scope="row">${e.id}</th>
                <td>${e.name}</td>
                <td>${e.city}</td>
                <td>${e.stadiumname}</td>
                <td>${e.points}</td>
                <td>
                    <div>
                        <a type="button" class="btn btn-danger" href="/championships/${championship.id}/teams/remove/${e.id}" >Remove</a>
                        <a type="button" class="btn btn-primary" href="/championships/${championship.id}/teams/${e.id}/match" >Create match</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <!--<button type="button" class="button btn btn-warning center-block">To list</button>-->
    <a type="button" href="/championships/" class="btn btn-warning">To list</a>

</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script><script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
