from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory data store for demonstration purposes
data_store = {
    "customers": [
        {
            "id": 1,
            "firstName": "Lorum",
            "lastName": "Ipsum",
            "email": "lorum.imsum@email.com",
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
    ]
}


@app.route("/customers", methods=["GET"])
def get_customers():
    """Returns all customers in the data_store."""
    return jsonify(data_store["customers"])


@app.route("/customers/<int:customer_id>", methods=["GET"])
def get_customer(customer_id):
    """Returns a specific customer by its ID."""
    customer = next(
        (
            customer
            for customer in data_store["customers"]
            if customer["id"] == customer_id
        ),
        None,
    )
    if customer:
        return jsonify(customer)
    return jsonify({"message": "Customer not found"}), 404


if __name__ == "__main__":
    app.run(debug=True)
