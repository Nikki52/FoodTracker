import csv
import json
import re
import os
import glob

def clean_text(name):
    name = re.sub(r'(?i)\bchapti\b', 'chapati', name)
    name = re.sub(r'(?i)chapati/roti', 'Chapati', name)
    return name.strip()

def process_csv_file(filename, base_foods):
    print(f"Processing {filename}...")
    with open(filename, 'r', encoding='utf-8', errors='ignore') as f:
        reader = csv.DictReader(f)
        count = 0
        for row in reader:
            try:
                # Convert keys to lowercase for robust lookup
                row_lower = {k.lower().strip() if k else "": v for k, v in row.items()}
                
                # Dynamically try to find the Food Name column
                name = clean_text(row_lower.get('dish name') or row_lower.get('food_name') or row_lower.get('name') or '')
                if not name:
                    continue
                
                # Dynamically try to find the nutrition columns
                cal = float(row_lower.get('calories (kcal)') or row_lower.get('energy_kcal') or row_lower.get('calories') or 0)
                carb = float(row_lower.get('carbohydrates (g)') or row_lower.get('carb_g') or row_lower.get('carbs') or 0)
                prot = float(row_lower.get('protein (g)') or row_lower.get('protein_g') or row_lower.get('protein') or 0)
                fat = float(row_lower.get('fats (g)') or row_lower.get('fat_g') or row_lower.get('fat') or 0)
                fiber = float(row_lower.get('fibre (g)') or row_lower.get('fibre_g') or row_lower.get('fiber') or 0)
                
                base_foods.append({
                    "name": name,
                    "servingSize": "1 serving",
                    "calories": int(cal),
                    "protein": round(prot, 1),
                    "carbs": round(carb, 1),
                    "fat": round(fat, 1),
                    "fiber": round(fiber, 1),
                    "category": "Indian Foods",
                    "source": "LocalImport"
                })
                count += 1
            except Exception as e:
                pass
        print(f" -> Found {count} items in {filename}")

def main():
    base_foods = []
    
    # Process ALL CSV files in the current directory automatically
    csv_files = glob.glob('*.csv')
    if not csv_files:
        print("No CSV files found in the directory.")
        return

    for file in csv_files:
        process_csv_file(file, base_foods)
        
    print(f"Processed {len(base_foods)} raw food items from CSVs.")

    # Deduplicate by name
    seen = set()
    unique_foods = []
    for food in base_foods:
        if food['name'].lower() not in seen:
            seen.add(food['name'].lower())
            unique_foods.append(food)
            
            if 'chapati' in food['name'].lower():
                roti_food = food.copy()
                roti_food['name'] = food['name'].lower().replace('chapati', 'Roti').title()
                if roti_food['name'].lower() not in seen:
                    seen.add(roti_food['name'].lower())
                    unique_foods.append(roti_food)

    # Generate combinations
    chapati = next((f for f in unique_foods if f['name'].lower() == 'chapati' or f['name'].lower() == 'roti'), None)
    rice = next((f for f in unique_foods if 'rice' in f['name'].lower() and 'boiled' in f['name'].lower()), None)
    if not rice:
        rice = next((f for f in unique_foods if 'rice' in f['name'].lower()), None)
    
    combinations = []
    for food in unique_foods:
        name_lower = food['name'].lower()
        if 'curry' in name_lower or 'dal' in name_lower or 'paneer' in name_lower or 'sabzi' in name_lower or 'masala' in name_lower:
            if chapati:
                combinations.append({
                    "name": f"{food['name']} with 2 Chapatis",
                    "servingSize": "1 portion + 2 pieces",
                    "calories": food['calories'] + 2 * chapati['calories'],
                    "protein": round(food['protein'] + 2 * chapati['protein'], 1),
                    "carbs": round(food['carbs'] + 2 * chapati['carbs'], 1),
                    "fat": round(food['fat'] + 2 * chapati['fat'], 1),
                    "fiber": round(food['fiber'] + 2 * chapati['fiber'], 1),
                    "category": "Indian Combos",
                    "source": "LocalImport"
                })
            if rice:
                combinations.append({
                    "name": f"{food['name']} with Rice Portion",
                    "servingSize": "1 portion + 1 plate",
                    "calories": food['calories'] + rice['calories'],
                    "protein": round(food['protein'] + rice['protein'], 1),
                    "carbs": round(food['carbs'] + rice['carbs'], 1),
                    "fat": round(food['fat'] + rice['fat'], 1),
                    "fiber": round(food['fiber'] + rice['fiber'], 1),
                    "category": "Indian Combos",
                    "source": "LocalImport"
                })
                
    unique_foods.extend(combinations)

    # Read existing
    output_file = 'app/src/main/assets/external_foods.json'
    existing_foods = []
    if os.path.exists(output_file):
        try:
            with open(output_file, 'r', encoding='utf-8') as f:
                existing_foods = json.load(f)
        except Exception:
            pass
            
    existing_names = {f['name'].lower() for f in existing_foods}
    new_foods = [f for f in unique_foods if f['name'].lower() not in existing_names]
    existing_foods.extend(new_foods)
    
    os.makedirs(os.path.dirname(output_file), exist_ok=True)
    with open(output_file, 'w', encoding='utf-8') as f:
        json.dump(existing_foods, f, indent=2)
        
    print(f"Successfully wrote {len(existing_foods)} total foods to {output_file} ({len(new_foods)} new items added).")

if __name__ == "__main__":
    main()
