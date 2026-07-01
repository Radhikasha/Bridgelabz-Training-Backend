<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>

<html>

<head>

    <title>Edit Employee</title>

    <style>

        body{

            font-family:Arial;
            background:#f5f5f5;

        }

        .container{

            width:500px;
            margin:50px auto;
            background:white;
            padding:25px;
            border-radius:10px;
            box-shadow:0px 0px 10px gray;

        }

        input,textarea{

            width:100%;
            padding:10px;
            margin-top:10px;

        }

        button{

            margin-top:20px;
            padding:10px 20px;
            background:#2196F3;
            color:white;
            border:none;
            cursor:pointer;

        }

    </style>

</head>

<body>

<div class="container">

    <h2 align="center">

        Update Employee

    </h2>

    <form action="employee?action=update" method="post">

        <input
                type="hidden"
                name="id"
                value="${employee.id}">

        <label>Employee Name</label>

        <input
                type="text"
                value="${employee.name}"
                readonly>

        <label>Salary</label>

        <input
                type="number"
                step="0.01"
                name="salary"
                value="${employee.salary}"
                required>

        <label>Notes</label>

        <textarea
                name="notes"
                rows="6">${employee.notes}</textarea>

        <br>

        <button type="submit">

            Update Employee

        </button>

    </form>

    <br>

    <a href="employee?action=list">

        Back

    </a>

</div>

</body>

</html>