from flask import Flask, jsonify, request

app = Flask(__name__)

# In-memory data store for demonstration purposes
data_store = {
    "items": [
        {"id": 1, "name": "Item A"},
        {"id": 2, "name": "Item B"}
    ]
}

@app.route('/items', methods=['GET'])
def get_items():
    """Returns all items in the data_store."""
    return jsonify(data_store['items'])

@app.route('/items/<int:item_id>', methods=['GET'])
def get_item(item_id):
    """Returns a specific item by its ID."""
    item = next((item for item in data_store['items'] if item['id'] == item_id), None)
    if item:
        return jsonify(item)
    return jsonify({"message": "Item not found"}), 404

@app.route('/items', methods=['POST'])
def create_item():
    """Creates a new item from the request body."""
    if not request.json or 'name' not in request.json:
        return jsonify({"message": "Invalid request: 'name' is required"}), 400
    
    new_id = max([item['id'] for item in data_store['items']]) + 1 if data_store['items'] else 1
    new_item = {"id": new_id, "name": request.json['name']}
    data_store['items'].append(new_item)
    return jsonify(new_item), 201

@app.route('/items/<int:item_id>', methods=['PUT'])
def update_item(item_id):
    """Updates an existing item by its ID."""
    item = next((item for item in data_store['items'] if item['id'] == item_id), None)
    if not item:
        return jsonify({"message": "Item not found"}), 404
    
    if not request.json or 'name' not in request.json:
        return jsonify({"message": "Invalid request: 'name' is required"}), 400
    
    item['name'] = request.json['name']
    return jsonify(item)

@app.route('/items/<int:item_id>', methods=['DELETE'])
def delete_item(item_id):
    """Deletes an item by its ID."""
    global data_store
    initial_length = len(data_store['items'])
    data_store['items'] = [item for item in data_store['items'] if item['id'] != item_id]
    if len(data_store['items']) < initial_length:
        return jsonify({"message": "Item deleted"})
    return jsonify({"message": "Item not found"}), 404

if __name__ == '__main__':
    app.run(debug=True)