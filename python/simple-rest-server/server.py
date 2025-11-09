from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory data store for demonstration purposes
data_store = {
    "customers": [
        {
            "id": 1,
            "firstName": "Lorum",
            "lastName": "Ipsum",
            "email": "lorum.ipsum@email.com",
            "phoneNumber": "333-333-3333",
        },
        {
            "id": 2,
            "firstName": "Abcde",
            "lastName": "Hendrix",
            "email": "abcde.hendrix@email.com",
            "phoneNumber": "256-123-4567",
        },
        {
            "id": 3,
            "firstName": "Leo",
            "lastName": "Yui",
            "email": "leo.yui@email.com",
            "phoneNumber": "123-456-7890",
        },
        {
            "id": 4,
            "firstName": "Bill",
            "lastName": "George",
            "email": "bill.george@email.com",
            "phoneNumber": "222-222-2222",
        },
        {
            "id": 5,
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@email.com",
            "phoneNumber": "256-867-5309",
        },
    ]
}


@app.route("/customers", methods=["GET"])
def get_customers():
    """Returns all customers in the data_store."""
    firstName = request.args.get("firstName")
    lastName = request.args.get("lastName")
    email = request.args.get("email")
    phoneNumber = request.args.get("phoneNumber")

    results = []

    for customer in data_store["customers"]:
        firstNameMatch = firstName is None or firstName == customer["firstName"]
        lastNameMatch = lastName is None or lastName == customer["lastName"]
        emailMatch = email is None or email == customer["email"]
        phoneNumberMatch = phoneNumber is None or phoneNumber == customer["phoneNumber"]

        if firstNameMatch and lastNameMatch and emailMatch and phoneNumberMatch:
            results.append(customer)

    if not results:
        return jsonify({"message": "Customer not found"}), 404

    return jsonify(results)


if __name__ == "__main__":
    app.run(debug=True)
