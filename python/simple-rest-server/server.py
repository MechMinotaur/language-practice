from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory data store for demonstration purposes
data_store = {
    "customers": [
        {
            "id": 1,
            "social": 444444444,
            "firstName": "Lorum",
            "lastName": "Ipsum",
            "email": "lorum.ipsum@email.com",
            "phoneNumber": "333-333-3333",
        },
        {
            "id": 2,
            "social": 333333333,
            "firstName": "Abcde",
            "lastName": "Hendrix",
            "email": "abcde.hendrix@email.com",
            "phoneNumber": "256-123-4567",
        },
        {
            "id": 3,
            "social": 111111111,
            "firstName": "Leo",
            "lastName": "Yui",
            "email": "leo.yui@email.com",
            "phoneNumber": "123-456-7890",
        },
        {
            "id": 4,
            "social": 222222222,
            "firstName": "Bill",
            "lastName": "George",
            "email": "bill.george@email.com",
            "phoneNumber": "222-222-2222",
        },
        {
            "id": 5,
            "social": 123456789,
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@email.com",
            "phoneNumber": "256-867-5309",
        },
    ]
}


@app.route("/customers", methods=["GET"])
def get_customers():

    def get_param_list(param_name):
        """Return list of lowercase values if provided, else None."""
        value = request.args.get(param_name)
        if value:
            return [v.strip().lower() for v in value.split(",")]
        return None

    # Extract query params (case-insensitive lists)
    socials = get_param_list("social")
    firstNames = get_param_list("firstName")
    lastNames = get_param_list("lastName")
    emails = get_param_list("email")
    phoneNumbers = get_param_list("phoneNumber")

    # AND (default) vs OR mode
    mode = request.args.get("mode", "and").lower()
    if mode not in ("and", "or"):
        return jsonify({"message": "Invalid mode. Use 'and' or 'or'."}), 400

    results = []

    for customer in data_store["customers"]:
        # Prepare customer fields in lowercase for comparison
        c_so = customer["social"]
        c_fn = customer["firstName"].lower()
        c_ln = customer["lastName"].lower()
        c_email = customer["email"].lower()
        c_phone = customer["phoneNumber"].lower()

        checks = []

        if socials is not None:
            checks.append(c_so in socials)

        if firstNames is not None:
            checks.append(c_fn in firstNames)

        if lastNames is not None:
            checks.append(c_ln in lastNames)

        if emails is not None:
            checks.append(c_email in emails)

        if phoneNumbers is not None:
            checks.append(c_phone in phoneNumbers)

        # Determine match based on AND/OR logic
        if not checks:  # no filters at all
            results.append(customer)
        else:
            match = all(checks) if mode == "and" else any(checks)
            if match:
                results.append(customer)

    if not results:
        return jsonify({"message": "Customer not found"}), 404

    return jsonify(results)


if __name__ == "__main__":
    app.run(debug=True)
