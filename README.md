# Featherwand_AzureOpenAI_Plugin
The Feather Wand plugin is an AI-powered agent designed for Apache JMeter that brings a conversational interface (chatbot) directly into your testing environment. While it is often associated with Azure OpenAI or Anthropic’s Claude, it essentially acts as a "bridge" between your JMeter test plan and a Large Language Model (LLM)


1. Direct AI Integration in JMeter
Feather Wand adds an AI Chat Panel and a dedicated toolbar button to the JMeter GUI. Instead of switching to a browser to ask for help, you can chat with the AI directly within the tool to generate test elements or troubleshoot errors.

2. Context-Aware Commands (Intellisense)
The plugin uses specific "slash commands" that allow the AI to understand your current test plan structure:

@this: Sends the configuration of the currently selected JMeter element to the AI for analysis or explanation.

@optimize: Requests suggestions to improve the performance or structure of the selected element.

@lint: Automatically renames elements in your test plan to follow best practices for better readability.

@code: Helps extract and insert AI-generated Groovy or Java code snippets directly into JSR223 samplers.


3. "Bring Your Own Key" (BYOK) Model
Feather Wand is a free plugin, but it requires you to provide your own API credentials. You can configure it to use:

Azure OpenAI: By providing your Azure endpoint and API key.

OpenAI: Using a standard OpenAI API key.

Anthropic Claude: Using a Claude API key.

Ollama: Using a Ollama API key.




4. Smart Scripting Assistance
The plugin is particularly useful for:

Generating Groovy Scripts: If you need complex logic in a Pre-Processor or Post-Processor, the AI can write the script based on your requirements.

Element Suggestions: You can ask, "How do I model a login flow with CSRF tokens?" and it will suggest the necessary Thread Groups, HTTP Samplers, and Extractors.

Usage Statistics: Using the @usage command, it can show you how many suggestions it has provided and estimate the time saved during your scripting session




Configuring Azure OpenAI for the Feather Wand plugin involves moving beyond the default Anthropic/Claude settings and pointing the plugin to your specific Azure resource.

Since you are already interested in Azure OpenAI and performance testing, here is the most efficient way to set this up:


Step 1: Gather your Azure OpenAI Details
You will need three specific pieces of information from your Azure Portal:

API Key: Found under "Keys and Endpoint" in your Azure OpenAI resource.

Endpoint URL: Looks like https://YOUR_RESOURCE_NAME.openai.azure.com/.

Deployment Name: The custom name you gave your model (e.g., gpt-4o or my-testing-model) in the Azure AI Studio or Foundry.


Step 2: Configure JMeter Properties
The Feather Wand plugin reads its configuration from JMeter's property files. You have two ways to do this:

Option A: Edit user.properties (Recommended)
This is safer as it persists through JMeter updates.

Navigate to your JMeter folder: /bin/user.properties.

Open it with a text editor and add the following lines at the bottom:


# Enable OpenAI/Azure Provider
jmeter.ai.azure.endpoint=https://YOUR_RESOURCE_NAME.openai.azure.com/
jmeter.ai.azure.api.key=YOUR_API_KEY
jmeter.ai.azure.deployment=YOUR_DEPLOYMENT_NAME(gpt-5 or gpt-5-nano)

# Optional: Set the API version if required by your region
# openai.api.version=2024-02-15-preview



Option B: Use the "AI Config" Element (Newer Versions)
If you are using the latest version of the plugin, you can avoid property files entirely:

Right-click your Test Plan > Add > Config Element > AI Config.

In the Provider dropdown, select Azure OpenAI.

Paste your API Key, Endpoint, and Deployment Name directly into the GUI fields.

Note: This method stores the credentials inside your .jmx file, which is great for portability but be careful if you share the file or check it into Git.


Step 3: Verify the Connection
Restart JMeter (if you edited the properties file).

Click the Blue Feather icon in the top toolbar to open the AI Chat Panel.

Type a simple test message like Hello, are you connected via Azure?.

If it responds, try the context command: select an existing HTTP Request and type @this to see if it can analyze your Azure-specific configurations.
