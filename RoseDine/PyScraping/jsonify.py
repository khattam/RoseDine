import json

def parse_meal_data(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    meals_by_date = {}
    current_date = None
    current_meal = None

    for line in lines:
        line = line.strip()
        if 'Scraping' in line:
            # Extract the date and meal type directly after "Scraping" statement
            current_date = line.split(' for ')[1]
            current_meal = line.split(' ')[1]
            # Initialize the dictionary for this date if not already initialized
            if current_date not in meals_by_date:
                meals_by_date[current_date] = {}
            # Initialize the list for this meal type if not already initialized
            meals_by_date[current_date][current_meal] = []
        elif 'Item:' in line:
            # Extract the item details after "Item:"
            item = line.split('Item:')[1].strip()
            # Append the item to the current meal list
            if current_date and current_meal:
                meals_by_date[current_date][current_meal].append(item)

    return meals_by_date

def write_json(data, output_file):
    with open(output_file, 'w') as f:
        json.dump(data, f, indent=4)

# Main program execution
if __name__ == '__main__':
    file_path = 'output.txt'
    output_file = 'json_output.json'
    meal_data = parse_meal_data(file_path)
    write_json(meal_data, output_file)
    print(f"Data has been written to {output_file}")
