import * as React from 'react';
import { useState } from 'react';

import { SafeAreaView, TextInput, Platform, Button, Text, View, TouchableOpacity, StyleSheet } from 'react-native';
import { signInWithGoogle } from 'react-native-icloud-gdrive';
import Mode from '../../src/mode';

export default function App() {

  const [clientId, setClientId] = useState<string>("");
  const [mode, setMode] = useState<Mode>(Mode.Appdata);

  return (
    <SafeAreaView>
      {Platform.OS == 'android' && <>
        <Text>SignInWithGoogle</Text>
        <TextInput placeholder='clientID' value={clientId} onChangeText={setClientId}/>
        <View style={{flexDirection: "row", justifyContent: "space-evenly"}}>
          <TouchableOpacity onPress={()=>{setMode(Mode.Appdata)}} style={[styles.button, {backgroundColor: mode == Mode.Appdata ? "green" : "#2296f3"}]}><Text>AppData</Text></TouchableOpacity>
          <TouchableOpacity onPress={()=>{setMode(Mode.Documents)}} style={[styles.button, {backgroundColor: mode == Mode.Documents ? "green" : "#2296f3"}]}><Text>Documents</Text></TouchableOpacity>
          <TouchableOpacity onPress={()=>{setMode(Mode.Both)}} style={[styles.button, {backgroundColor: mode == Mode.Both ? "green" : "#2296f3"}]}><Text>Both</Text></TouchableOpacity>
        </View>
        <Button title='Setup' onPress={() => {
          signInWithGoogle(clientId, mode)
        }}/>
      </>}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  button: {
    padding: 3,
    textAlign: 'center',
    justifyContent: 'center',
    alignItems: 'center'
  }
});
