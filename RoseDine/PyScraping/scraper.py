from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from bs4 import BeautifulSoup
import time

def process_section(section, file):
    elements = section.find_all(['div', 'h3'], class_=['station-title-inline-block', 'site-panel__daypart-item'])
    current_section = None
    for element in elements:
        if 'station-title-inline-block' in element.get('class', []):
            section_name = element.find('h3', class_='site-panel__daypart-station-title').text.strip()
            current_section = section_name
            file.write(f"\tSection: {section_name}\n")
        elif 'site-panel__daypart-item' in element.get('class', []):
            if current_section:
                item_name = element.find('button', class_='site-panel__daypart-item-title').text.strip()
                file.write(f"\t  Item: {item_name}\n")

def get_meal(meal_name, url, driver, file):
    try:
        driver.get(url)
        wait = WebDriverWait(driver, 10)

        try:
            meal_button = wait.until(EC.presence_of_element_located((By.XPATH, f"//button[contains(., '{meal_name}')]")))
            meal_tab_id = meal_button.get_attribute('aria-controls')
            if meal_button.get_attribute('aria-selected') == "false":
                driver.execute_script("arguments[0].click();", meal_button)
                time.sleep(2)

            soup = BeautifulSoup(driver.page_source, 'html.parser')
            meal_section = soup.find(id=meal_tab_id)
            if meal_section:
                file.write(f"{meal_name}:\n")
                process_section(meal_section, file)
            else:
                file.write(f"No {meal_name} section found on {url}\n")
        except NoSuchElementException:
            file.write(f"No {meal_name} button found on {url}\n")
        except TimeoutException:
            file.write(f"Timeout waiting for {meal_name} button on {url}\n")

    except Exception as e:
        file.write(f"Error accessing {url}: {e}\n")
