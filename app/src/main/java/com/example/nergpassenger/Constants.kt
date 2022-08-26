package com.example.nergpassenger

object Constants {
    val IS_LOGIN ="ISLOGIN"
    val ACCESS_TOKEN="ACCESS_TOKEN"
    val ticket = "{\n" +
            "    \"ticketNumber\": \"SE1139\",\n" +
            "    \"bookedAt\": \"2022-08-09T04:24:42.720Z\",\n" +
            "    \"travelDate\": \"2022-08-09\",\n" +
            "    \"passengers\": [\n" +
            "      \"child\"\n" +
            "    ],\n" +
            "    \"type\": \"regional\",\n" +
            "    \"loyalty\": \"silver\",\n" +
            "    \"bookedBy\": {\n" +
            "      \"id\": 12,\n" +
            "      \"username\": \"Venkatreddy\",\n" +
            "      \"givenName\": \"Venkat\",\n" +
            "      \"familyName\": \"reddy\",\n" +
            "      \"birthdate\": \"2008-08-09\",\n" +
            "      \"cardType\": \"visa\",\n" +
            "      \"cardNumber\": \"1234567876543233\",\n" +
            "      \"cardExpiration\": \"12/22\",\n" +
            "      \"cardSecurityCode\": \"123\"\n" +
            "    },\n" +
            "    \"segments\": [\n" +
            "      {\n" +
            "        \"id\": 9,\n" +
            "        \"departureStop\": {\n" +
            "          \"id\": 1,\n" +
            "          \"time\": \"06:00:00\",\n" +
            "          \"delayInMinutes\": 30,\n" +
            "          \"station\": {\n" +
            "            \"code\": \"BER\",\n" +
            "            \"name\": \"Berlin Airport\",\n" +
            "            \"latitude\": 52.4802978,\n" +
            "            \"longitude\": 13.3886328\n" +
            "          },\n" +
            "          \"connection\": {\n" +
            "            \"trainNumber\": \"10201\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"arrivalStop\": {\n" +
            "          \"id\": 2,\n" +
            "          \"time\": \"07:45:00\",\n" +
            "          \"delayInMinutes\": 75,\n" +
            "          \"station\": {\n" +
            "            \"code\": \"NZ\",\n" +
            "            \"name\": \"Neustrelitz\",\n" +
            "            \"latitude\": 53.3617163,\n" +
            "            \"longitude\": 13.0630004\n" +
            "          },\n" +
            "          \"connection\": {\n" +
            "            \"trainNumber\": \"10201\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"seatReservations\": [\n" +
            "          {\n" +
            "            \"id\": 6,\n" +
            "            \"railroadCarNumber\": 5,\n" +
            "            \"seatNumber\": \"10A\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }"
}