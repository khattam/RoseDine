import requests

# API credentials
APP_ID = 'b1c25162'
APP_KEY = '3ca419138631e74ea4c9ce571e7f9020'
API_URL = 'https://api.edamam.com/api/nutrition-data'

def get_nutrition_data(ingredient):

    params = {
        'app_id': APP_ID,
        'app_key': APP_KEY,
        'nutrition-type': 'logging',
        'ingr': ingredient
    }
    response = requests.get(API_URL, params=params)
    if response.status_code == 200:
        return response.json()
    else:
        return None

def process_file(old_file, new_file):
    with open(old_file, 'r') as file:
        lines = file.readlines()

    with open(new_file, 'w') as file:
        for line in lines:
            file.write(line)
            if 'Item:' in line:
                item_name = line.split('Item:')[1].strip()
                nutrition_data = get_nutrition_data(item_name)
                if nutrition_data and 'totalNutrients' in nutrition_data:
                    try:
                        nutrients = nutrition_data['totalNutrients']
                        health_labels = nutrition_data['healthLabels']
                        file.write(f"   Calories: {nutrients.get('ENERC_KCAL', {'quantity': 0})['quantity']} kcal, "
                                   f"Fats: {nutrients.get('FAT', {'quantity': 0})['quantity']} g, "
                                   f"Proteins: {nutrients.get('PROCNT', {'quantity': 0})['quantity']} g, "
                                   f"Carbs: {nutrients.get('CHOCDF', {'quantity': 0})['quantity']} g\n")
                        file.write(f"   Vegan: {'VEGAN' in health_labels}, "
                                   f"Vegetarian: {'VEGETARIAN' in health_labels}, "
                                   f"Gluten-Free: {'GLUTEN_FREE' in health_labels}\n")
                    except KeyError:
                        file.write("   Nutritional data not available.\n")
                else:
                    file.write("   Failed to retrieve data or item not found in database.\n")

if __name__ == "__main__":
    process_file('output.txt', 'output_new.txt')
