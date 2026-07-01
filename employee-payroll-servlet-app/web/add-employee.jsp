<%@ page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>

<html>

<head>

    <title>Add Employee</title>

    <style>

        body{
            font-family:Arial;
            background:#f5f5f5;
        }

        .container{

            width:650px;
            margin:20px auto;
            background:white;
            padding:20px;
            border-radius:10px;
            box-shadow:0px 0px 10px gray;

        }

        input[type="text"],
        input[type="number"],
        input[type="date"],
        textarea {

            width: 100%;
            padding: 10px;
            margin-top: 10px;
            box-sizing: border-box;

        }

        input[type="radio"],
        input[type="checkbox"] {

            width: auto;
            margin-right: 6px;

        }

        button{

            padding:10px 20px;
            background:#4CAF50;
            color:white;
            border:none;
            cursor:pointer;

        }

        .section{

            margin-top:15px;

        }

    </style>

</head>

<body>

<div class="container">

    <h2 align="center">

        Employee Payroll Form

    </h2>

    <form action="employee?action=create" method="post">

        <label>Name</label>

        <input
                type="text"
                name="name"
                required>

        <div class="section">

            <label>Profile Image</label>

            <br><br>

            <input
                    type="radio"
                    name="profileImage"
                    value="ellipse-1.png"
                    checked>

            Image 1

            <input
                    type="radio"
                    name="profileImage"
                    value="ellipse-2.png">

            Image 2

            <input
                    type="radio"
                    name="profileImage"
                    value="ellipse-3.png">

            Image 3

            <input
                    type="radio"
                    name="profileImage"
                    value="ellipse-4.png">

            Image 4

        </div>

        <div class="section">

            <label>Gender</label>

            <br><br>

            <input
                    type="radio"
                    name="gender"
                    value="Male"
                    checked>

            Male

            <input
                    type="radio"
                    name="gender"
                    value="Female">

            Female

        </div>

        <div class="section">

            <label>Departments</label>

            <br><br>

            <input
                    type="checkbox"
                    name="departments"
                    value="HR">

            HR

            <input
                    type="checkbox"
                    name="departments"
                    value="Sales">

            Sales

            <input
                    type="checkbox"
                    name="departments"
                    value="Finance">

            Finance

            <input
                    type="checkbox"
                    name="departments"
                    value="Engineer">

            Engineer

            <input
                    type="checkbox"
                    name="departments"
                    value="Others">

            Others

        </div>

        <label>Salary</label>

        <input
                type="number"
                step="0.01"
                name="salary"
                required>

        <label>Start Date</label>

        <input
                type="date"
                name="startDate"
                required>

        <label>Notes</label>

        <textarea
                name="notes"
                rows="5">
</textarea>

        <br><br>

        <button type="submit">

            Save Employee

        </button>

    </form>

    <br>

    <a href="employee?action=list">

        Back

    </a>

</div>

</body>

</html>