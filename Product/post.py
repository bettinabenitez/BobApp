import psycopg2
import json
import collections
import pprint
import sys

from random import randint
from bottle import route, run, post, request, get

@get('/postdata')
def postdata():
    data = request.json
    GmailAddress = data['GmailAddress'] 
    FirstName = data['FirstName'] 
    LastName = data['LastName']
    Password = data['Password'] 
    UserType  = data['UserType']
    print  GmailAddress, FirstName, LastName, Password, UserType

    sql = """INSERT INTO "User"("GmailAddress", "FirstName", "LastName", "Password", "UserType") VALUES(%s, %s, %s, %s, %s);"""
    conn = None

    try:
        # read database configuration
        conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
        print "Connecting to database\n ->%s" % (conn_string)
        conn = psycopg2.connect(conn_string)
        cursor = conn.cursor()
        # execute the INSERT statement
        cursor.execute(sql, (GmailAddress ,FirstName, LastName, Password, UserType) )
        # commit the changes to the database 
        conn.commit()
        # close communication with the database
        cursor.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    finally:
        if conn is not None:
           conn.close()


@post('/login')
def login():
    #retrieves data from /login
    try:
        data = request.json
        GmailAddress = data['GmailAddress']
        Password = data['Password']
        print GmailAddress, Password

        conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
        print "Connecting to database\n ->%s" % (conn_string)
        conn = psycopg2.connect(conn_string)
        cursor = conn.cursor()

        #checks database
        cursor.execute("""SELECT * FROM "User" WHERE "GmailAddress"= %s AND "Password"= %s""", (GmailAddress,Password))
        rows = cursor.fetchone()
        print rows

        #if exists, create json access accepted which is read by android
        if rows is not None:
            print(rows)
            rows = cursor.fetchone()
            m = {'access':'accepted'}
            n = json.dumps(m)
            o = json.loads(n)
            print o
        conn.close()
        print (n)
        print "ended"
        return (n)
    except Exception as e:
        print("Error: " + str(e))

@post('/schedule')
def schedule():

    #played = 2
    #performing now = 0
    #queued = 1
    
    conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
    print "Connecting to database\n ->%s" % (conn_string)
    conn = psycopg2.connect(conn_string)
    cursor = conn.cursor()
    # selects the performer name the currently playing perfromer
    cursor.execute("""SELECT "Performer_Name" FROM "Performer" inner join "ScheduleEntry" ON "Performer"."PerformerId" = "ScheduleEntry"."PerformerId"  WHERE "Status" = 0 """)
    rows = cursor.fetchall()
    print rows

    #converts fetched query rows into JSON Array List
    objects_list = []
    for row in rows:
        d = collections.OrderedDict()
        d = row[0]
        objects_list.append(d)
    firstList = json.dumps(objects_list)
    print objects_list

    #selects the performer that is first on queue

    cursor.execute("""SELECT "Performer_Name" FROM "Performer" inner join "ScheduleEntry" ON "Performer"."PerformerId" = "ScheduleEntry"."PerformerId" WHERE "Status" = 1 ORDER BY "ScheduleId" ASC LIMIT 1""")
    nextUp = cursor.fetchall()
    # prints to terminal if the variable nextUp is used
    print nextUp
    for row in nextUp:
        e = collections.OrderedDict()
        e = row[0]
        objects_list.append(e)
    updatedList = json.dumps(objects_list) #adds performer to the existing list

    cursor.execute("""SELECT "Perfomer_id" FROM "ScheduleEntry" WHERE "Status" = 1 ORDER BY "ScheduleId" ASC LIMIT 1; """)
    performerId = cursor.fetchall()
    print performerId
    PID = performerId[0]
    print PID
    PIDSerialise= json.dumps(PID)
    PIDToString = json.loads(PIDSerialise)
    print PIDToString

    #changes the status of performer that is playing to played
    cursor.execute("""UPDATE "ScheduleEntry" SET "Status" = 2 WHERE "Status" in (SELECT "Status" FROM "ScheduleEntry" WHERE "Status" = 0 ORDER BY "ScheduleId" ASC LIMIT 1);""")
    #changes the status of the performer that is first on queue to playing
    cursor.execute("""UPDATE "ScheduleEntry" SET "Status" = 0 WHERE "Status" = 1  and "Perfomer_id" = %s """, (PIDToString))
    #commit the changes to the database 
    conn.commit()
    conn.close()
    
    #SEND JSON TO ANDROID SO ANDROID CAN READ IT
    return (updatedList)

@get('/raffle')
def raffle():

    conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
    print "Connecting to database\n ->%s" % (conn_string)
    conn = psycopg2.connect(conn_string)
    cursor = conn.cursor()

    cursor.execute("""SELECT "RaffleTicketNumber" FROM "RaffleTicket" WHERE "Status" = 0 ORDER BY "RaffleTicketNumber" DESC LIMIT 1;""")
    maxTickets = cursor.fetchone()
    print maxTickets
    maxTicketsSerialise= json.dumps(maxTickets[0])
    maxTicketsString = json.loads(maxTicketsSerialise)
    print maxTicketsString
    ticketsMax = int(maxTicketsString)

    winner = str(randint(0, ticketsMax))
    print winner


    cursor.execute("""SELECT "FirstName" , "LastName" FROM "User" inner join "RaffleTicket" ON "User"."User_Id" = "RaffleTicket"."UserId" WHERE "RaffleTicketNumber" = %s """, (winner))
    rows = cursor.fetchall()

    print rows

    objects_list = []
    for row in rows:
        d = collections.OrderedDict()
        d['FirstName'] = row[0]
        d['LastName'] = row[1]
        objects_list.append(d)
    selectedWinner = json.dumps(objects_list)
    print selectedWinner

    
    conn.close()
    return(selectedWinner)

    # try:
    #     # read database configuration
    #     conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
    #     print "Connecting to database\n ->%s" % (conn_string)
    #     conn = psycopg2.connect(conn_string)
    #     cursor = conn.cursor()
    #     # execute the INSERT statement
    #     #changes the status to done
    #     cursor.execute("""UPDATE "Raffle" WHERE "Status" <> %d""",  (2, Schedule) )
    #     # commit the changes to the database 
    #     conn.commit()
    #     # close communication with the database
    #     cursor.close()
    # except (Exception, psycopg2.DatabaseError) as error:
    #     print(error)
    # finally:
    #     if conn is not None:
    #         conn.close()


@post('/voting')
def voting():

    try:
        data = request.json
        GmailAddress = data['GmailAddress']
        BandVote = data['BandVote']
        VocalVote = data['VocalVote']
        print GmailAddress, BandVote, VocalVote

        conn_string = "host='localhost' dbname='bobapp' user='postgres' password='pkolij'"
        print "Connecting to database\n ->%s" % (conn_string)
        conn = psycopg2.connect(conn_string)
        cursor = conn.cursor()

        #checks database
        cursor.execute("""INSERT INTO "BandVote"("User_Email", "Performer_Name") VALUES(%s, %s) """, (GmailAddress,BandVote))
        cursor.execute("""INSERT INTO "VocalVote"("User_Email", "Performer_Name") VALUES(%s, %s) """, (GmailAddress,VocalVote))
        conn.commit()
        

    except Exception as e:
        print("Error: " + str(e))



run(host='localhost', port=8080)


