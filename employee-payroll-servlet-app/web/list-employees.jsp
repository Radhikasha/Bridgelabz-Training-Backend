<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<html>

<head>

    <title>Employee List</title>

    <style>

        body{

            font-family:Arial;
            background:#f5f5f5;
            margin:0;
            padding:0;

        }

        .header{

            background:#2196F3;
            color:white;
            padding:15px;

            display:flex;
            justify-content:space-between;
            align-items:center;

        }

        .header a{

            color:white;
            text-decoration:none;
            margin-left:15px;
            font-weight:bold;

        }

        .container{

            width:95%;
            margin:auto;
            margin-top:20px;

        }

        table{

            width:100%;
            border-collapse:collapse;
            background:white;

        }

        th{

            background:#1976D2;
            color:white;
            padding:12px;

        }

        td{

            padding:10px;
            border:1px solid #ddd;
            text-align:center;

        }

        img{

            width:60px;
            height:60px;
            border-radius:50%;

        }

        .dept{

            display:inline-block;
            padding:5px 10px;
            background:#4CAF50;
            color:white;
            border-radius:15px;
            margin:2px;
            font-size:12px;

        }

        .edit{

            background:orange;
            color:white;
            padding:5px 10px;
            text-decoration:none;
            border-radius:5px;

        }

        .delete{

            background:red;
            color:white;
            padding:5px 10px;
            text-decoration:none;
            border-radius:5px;

        }

        .add{

            background:green;
            color:white;
            padding:10px 15px;
            text-decoration:none;
            border-radius:5px;

        }

    </style>

</head>

<body>

<div class="header">

    <div>

        <h2>

            Employee Payroll Application

        </h2>

    </div>

    <div>

        Welcome

        <b>

            ${currentUser.username}

        </b>

        |

        ${currentUser.role}

        <a href="logout">

            Logout

        </a>

    </div>

</div>

<div class="container">

<c:if test="${currentUser.role=='ADMIN'}">

    <a
            class="add"
            href="employee?action=add">

        Add Employee

    </a>

    <br><br>

    <table>

        <tr>

            <th>Image</th>

            <th>Name</th>

            <th>Gender</th>

            <th>Departments</th>

            <th>Salary</th>

            <th>Start Date</th>

            <th>Notes</th>

            <th>Action</th>

        </tr>

        <c:forEach
                var="emp"
                items="${employees}">

            <tr>

                <td>

                    <img src="${pageContext.request.contextPath}/images/${emp.profileImage}"
                         width="60"
                         height="60"
                         alt="Profile">

                </td>

                <td>

                        ${emp.name}

                </td>

                <td>

                        ${emp.gender}

                </td>

                <td>

                    <c:forEach
                            var="dept"
                            items="${emp.departments}">

<span class="dept">

        ${dept}

</span>

                    </c:forEach>

                </td>

                <td>

                    ₹ ${emp.salary}

                </td>

                <td>

                        ${emp.startDate}

                </td>

                <td>

                        ${emp.notes}

                </td>

                <td>

                    <a
                            class="edit"
                            href="employee?action=edit&id=${emp.id}">

                        Edit

                    </a>

                    <a
                            class="delete"
                            href="employee?action=delete&id=${emp.id}">

                        Delete

                    </a>

                </td>

            </tr>

        </c:forEach>

    </table>

</c:if>

    <c:if test="${currentUser.role == 'USER'}">

        <style>
            .user-dashboard{
                width:60%;
                margin:50px auto;
                background:#ffffff;
                border-radius:12px;
                padding:40px;
                box-shadow:0 5px 15px rgba(0,0,0,0.2);
                text-align:center;
            }

            .user-dashboard h1{
                color:#1976D2;
                margin-bottom:10px;
            }

            .user-dashboard h3{
                color:#555;
                margin-bottom:25px;
            }

            .user-card{
                background:#f4f8fb;
                padding:25px;
                border-radius:10px;
                margin-top:20px;
            }

            .user-card table{
                width:100%;
                border-collapse:collapse;
            }

            .user-card td{
                padding:12px;
                font-size:18px;
                border:none;
                text-align:left;
            }

            .label{
                font-weight:bold;
                width:35%;
                color:#1976D2;
            }

            .status{
                margin-top:25px;
                color:green;
                font-weight:bold;
                font-size:18px;
            }

            .note{
                margin-top:20px;
                color:#666;
                font-size:16px;
            }

            .logout-btn{
                margin-top:30px;
                display:inline-block;
                background:#1976D2;
                color:white;
                text-decoration:none;
                padding:12px 25px;
                border-radius:8px;
                font-weight:bold;
            }

            .logout-btn:hover{
                background:#0d5db8;
            }

        </style>

        <div class="user-dashboard">

            <h1>Employee Payroll System</h1>

            <h3>Welcome, ${currentUser.username} 👋</h3>

            <div class="user-card">

                <table>

                    <tr>
                        <td class="label">Username</td>
                        <td>${currentUser.username}</td>
                    </tr>

                    <tr>
                        <td class="label">Email</td>
                        <td>${currentUser.email}</td>
                    </tr>

                    <tr>
                        <td class="label">Role</td>
                        <td>${currentUser.role}</td>
                    </tr>

                </table>

            </div>

            <div class="status">
                ✔ Login Successful
            </div>

            <div class="note">
                Your account has USER privileges.<br><br>
                Only the Administrator can add, update or delete employee records.
            </div>

            <a href="logout" class="logout-btn">
                Logout
            </a>

        </div>

    </c:if>

    <c:if test="${not empty audits}">

        <br><br>

        <h2>

            Payroll Audit Logs

        </h2>

        <table>

            <tr>

                <th>ID</th>

                <th>Employee ID</th>

                <th>Action</th>

                <th>Old Salary</th>

                <th>New Salary</th>

                <th>Changed By</th>

                <th>Changed At</th>

            </tr>

            <c:forEach
                    var="audit"
                    items="${audits}">

                <tr>

                    <td>${audit.id}</td>

                    <td>${audit.employee_id}</td>

                    <td>${audit.action_type}</td>

                    <td>${audit.old_salary}</td>

                    <td>${audit.new_salary}</td>

                    <td>${audit.changed_by}</td>

                    <td>${audit.changed_at}</td>

                </tr>

            </c:forEach>

        </table>

    </c:if>

</div>

</body>

</html>