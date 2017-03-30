#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>

using namespace std;

class tag{
public:
	tag(string _tagname) : tagname(_tagname) {}

	string tagname = ""; // rect or circle or something
	string csvname = ""; // binded csv file value name
	int csvindex = 0; // csv file index
	vector<pair<string, string>> attr; // attribute name, value pair
	vector<tag*> children;
	tag* parent = nullptr;
	bool valid = true;
	void Add(tag* child){
		child->parent = this;
		children.push_back(child);
	}
	tag* Find(string name){
		for(auto c : children){
			if(c->tagname == name)	return c;
		}
		return nullptr;
	}
	void print(ofstream &ofs){
		if(valid){
			ofs << "<" << tagname;
			for(auto &a : attr){
				ofs << " " << a.first << "=\"" << a.second << "\"";
			}
			ofs << ">" << '\n';
			for(tag* c : children){
				c->print(ofs);
			}
			ofs << "</" << tagname << ">" << '\n';
		}
	}
};

class csvdb{
public:
	csvdb() = default;
	vector<string> fieldname;
	vector<string> fieldtype;
	map<string, vector<string>> data;
	
	string Find(string cname, string fname){
		int i = 0;
		for(;i < fieldname.size(); i++){
			if(fieldname[i] == fname)	break;
		}
		return data[cname][i];
	}
};

vector<csvdb> db;

void csvprocess(ifstream &finput){ 
	csvdb tempdb;
	string line;
	vector<string> split;
	bool first = true; bool second = true;
	while(getline(finput, line)){
		string temp = "";
		for(int i = 0; i < line.length(); i++){
			if(line[i] == ','){
				split.push_back(temp);
				temp = "";
			}
			else	temp += line[i];
		}
		split.push_back(temp);
		
		if(first){
			tempdb.fieldname = split;
			first = false;
		}
		else if(second){
			tempdb.fieldtype = split;
			second = false;
		}
		else	tempdb.data.insert(pair<string, vector<string>>(split[0], split));
		split.clear();
	}
	db.push_back(tempdb);
}

tag root_tag("html");
tag* selectscope = &root_tag;
vector<tag*> selection;
string tname = "";
bool sltchk;
bool exceptionchk = false;

int main(int argc, char* argv[]){
	
	for(int i = 1; i < argc+1; i++){
		ifstream finput(argv[i]);
		csvprocess(finput);
	}
	
	string input;
	while(1){
		getline(cin, input);
		string temp = "";
		vector<string> split;
		for(int i = 0; i < input.length(); i++){
			if(input[i] == ' '){
				split.push_back(temp);
				temp = "";
			}
			else	temp += input[i];
		}
		split.push_back(temp); // split input line
		
		if(!split[0].compare("append")){
			tag* addtag = new tag(split[1]); // svg tag name
			selectscope->Add(addtag);
			selectscope = addtag;
			sltchk = true; // check whether single or multiple selection
		}
		else if(split[0] == "select"){
			selectscope = selectscope->Find(split[1]); 
			sltchk = true;
		}
		else if(split[0] == "selectAll"){
			tname = split[1]; //svg-tag-name
			for(auto c : selectscope->children){
				if(c->tagname == tname) selection.push_back(c);
			}
			if(selection.empty())	exceptionchk = true;
			sltchk = false;
		}
		else if(split[0] == "remove"){
				sltchk = true;
				exceptionchk = false;
				if(selection.empty())	continue;
				else{
					for(auto c : selection) 	c->valid = false;
					selection.clear();
				}
		}
		else if(split[0] == "end"){
			sltchk = true;
			if(selection.empty() && !exceptionchk){
				if(selectscope->parent == nullptr)	break;
				else{
					selectscope = selectscope->parent;
					selection.clear();
				}
			}
			else{
				selection.clear();
				exceptionchk = false;
			}
		}
		else if(split[0] == "enter"){
			int i = stoi(split[1]); i--; // csv-index
			vector<tag*> newselection;
			for(auto &p : db[i].data){
				bool valid = true;
				for(tag* sel : selection)	if(p.first == sel->csvname)	valid = false;
				if(valid){
					tag* ctag = new tag(tname);
					ctag->csvname = p.first; ctag->csvindex = i;
					selectscope->Add(ctag);
					newselection.push_back(ctag);
				}
			}
			selection = newselection;
		}
		else if(split[0] == "update"){
			int i = stoi(split[1]); i--; // csv-index
			vector<tag*> newselection;
			for(auto c : selection){
				bool valid = false;
				string cname = "";
				for(auto &p : db[i].data){
					if(c->csvname == p.first){
						cname = p.first;
						valid = true;
					}
				}
				if(valid){
					c->csvindex = i;
					newselection.push_back(c); // 여기 조심 
				}
			}
			selection = newselection;
		}
		else if(split[0] == "exit"){
			int i = stoi(split[1]); i--; // csv-index
			vector<tag*> newselection;
			for(auto c : selection){
				bool valid = true;
				string cname = "";
				for(auto &p : db[i].data){
					if(c->csvname == p.first){
						cname = p.first;
						valid = false;
					}
				}
				if(valid)	newselection.push_back(c);
			}
			selection = newselection;
		}
		else if(split[0] == "cattr"){
			string input1 = split[1]; // attr-name
			string input2 = split[2]; // attr-value
			if(sltchk){
				bool chk = true;
				for(auto &att : selectscope->attr){
					if(input1 == att.first){
						att.second = input2; // modify
						chk = false;
					}
				}
				if(chk){
					pair<string, string> p(input1, input2); // new 
					selectscope->attr.push_back(p);
				}
			}
			else{
				for(auto c : selection){
					bool chk = true;
					for(auto &att : c->attr){
						if(input1 == att.first){
							att.second = input2; // modify
							chk = false;
						}
					}
					if(chk){
						pair<string, string> p(input1, input2); // new 
						c->attr.push_back(p);
					}
				}
			}
		}
		else if(split[0] == "tattr"){
			int x = stoi(split[1]); 
			int y = stoi(split[2]);
			for(int i = 0; i < selection.size(); i++){
				bool chk = true;
				string transval = "translate(" + to_string(x*i) + "," + to_string(y*i) + ")";
				for(auto &att : selection[i]->attr){
					if(att.first == "transform"){
						att.second = transval;
						chk = false;
					}
				}
				if(chk){
					pair<string, string> p("transform",transval);
					selection[i]->attr.push_back(p);
				}
			}
		}
		else if(split[0] == "dattr"){
			for(int i = 0; i < selection.size(); i++){
				string value = db[selection[i]->csvindex].Find(selection[i]->csvname, split[2]);
				double val;
				if(split.size() == 5){
					val = stod(value);
					val = val * stod(split[3]) + stod(split[4]);
				}
				else if(split.size() == 4){
					val = stod(value);
					val = val * stod(split[3]);
				}
				bool chk = true;
				for(auto &att : selection[i]->attr){
					if(att.first == split[1]){
						if(split.size() == 3)	att.second = value;
						else att.second = to_string(val);
						chk = false;
					}
				}
				if(chk){
					if(split.size() != 3)	value = to_string(val); // modify
					pair<string, string> p(split[1], value);
					selection[i]->attr.push_back(p);
				}
			}
		}
		else if(!split[0].compare("print")){
			ofstream ofs(split[1]);
			root_tag.print(ofs);
		}
	}
	return 0;
}
